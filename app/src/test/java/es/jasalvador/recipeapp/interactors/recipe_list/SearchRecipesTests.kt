package es.jasalvador.recipeapp.interactors.recipe_list

import com.google.gson.Gson
import es.jasalvador.recipeapp.cache.AppDatabaseFake
import es.jasalvador.recipeapp.cache.RecipeDao
import es.jasalvador.recipeapp.cache.RecipeDaoFake
import es.jasalvador.recipeapp.cache.model.RecipeEntityMapper
import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.network.RecipeService
import es.jasalvador.recipeapp.network.data.MockWebServerResponses.recipeListResponse
import es.jasalvador.recipeapp.network.model.RecipeDtoMapper
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.net.HttpURLConnection

class SearchRecipesTests {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val appDatabase = AppDatabaseFake()

    private val DUMMY_TOKEN = "fake-token"
    private val DUMMY_QUERY = "fake-query"

    private lateinit var searchRecipes: SearchRecipes

    private lateinit var recipeService: RecipeService
    private lateinit var recipeDao: RecipeDao
    private val dtoMapper = RecipeDtoMapper()
    private val entityMapper = RecipeEntityMapper()

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/recipe/")
        recipeService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create()

        recipeDao = RecipeDaoFake(appDatabase)

        searchRecipes = SearchRecipes(recipeService, recipeDao, dtoMapper, entityMapper)
    }

    @Test
    fun getRecipesFromNetwork_emitRecipesFromCache(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(recipeListResponse)
        )

        assert(recipeDao.getAllRecipes(1, 30).isEmpty())

        val flowItems = searchRecipes.execute(DUMMY_TOKEN, 1, DUMMY_QUERY, true).toList()

        assert(recipeDao.getAllRecipes(1, 30).isNotEmpty())

        assert(flowItems[0].loading)

        val recipes = flowItems[1].data
        assert(recipes?.size ?: 0 > 0)

        assert(recipes?.get(index = 0) is Recipe)

        assert(!flowItems[1].loading)
    }

    @Test
    fun getRecipesFromNetwork_emitHttpError(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        val flowItems = searchRecipes.execute(DUMMY_TOKEN, 1, DUMMY_QUERY, true).toList()

        assert(flowItems[0].loading)

        val error = flowItems[1].error
        assert(error != null)

        assert(!flowItems[1].loading)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
