package es.jasalvador.recipeapp.interactors.recipe

import com.google.gson.GsonBuilder
import es.jasalvador.recipeapp.cache.AppDatabaseFake
import es.jasalvador.recipeapp.cache.RecipeDao
import es.jasalvador.recipeapp.cache.RecipeDaoFake
import es.jasalvador.recipeapp.cache.model.RecipeEntityMapper
import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.interactors.recipe_list.SearchRecipes
import es.jasalvador.recipeapp.network.RecipeService
import es.jasalvador.recipeapp.network.data.MockWebServerResponses
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
import java.net.HttpURLConnection

class GetRecipeTests {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val appDatabase = AppDatabaseFake()

    private val DUMMY_TOKEN = "fake-token"
    private val DUMMY_QUERY = "fake-query"

    private lateinit var getRecipe: GetRecipe
    private val RECIPE_ID = 1551

    private lateinit var recipeService: RecipeService
    private lateinit var recipeDao: RecipeDao
    private val dtoMapper = RecipeDtoMapper()
    private val entityMapper = RecipeEntityMapper()
    private lateinit var searchRecipes: SearchRecipes

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/recipe/")
        recipeService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RecipeService::class.java)
        recipeDao = RecipeDaoFake(appDatabase)
        searchRecipes = SearchRecipes(recipeService, recipeDao, dtoMapper, entityMapper)

        getRecipe = GetRecipe(
            recipeDao = recipeDao,
            entityMapper = entityMapper,
            recipeService = recipeService,
            dtoMapper = dtoMapper,
        )
    }

    @Test
    fun getRecipesFromNetwork_getRecipeById(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponses.recipeListResponse)
        )

        assert(recipeDao.getAllRecipes(1, 30).isEmpty())

        searchRecipes.execute(DUMMY_TOKEN, 1, DUMMY_QUERY, true).toList()

        assert(recipeDao.getAllRecipes(1, 30).isNotEmpty())

        val recipeAsFlow = getRecipe.execute(DUMMY_TOKEN, RECIPE_ID, true).toList()

        assert(recipeAsFlow[0].loading)

        val recipe = recipeAsFlow[1].data
        assert(recipe?.id == RECIPE_ID)

        assert(recipe is Recipe)
        assert(!recipeAsFlow[1].loading)
    }


    @Test
    fun attemptGetNullRecipeFromCache_getRecipeById(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponses.recipeWithId1551)
        )

        assert(recipeDao.getAllRecipes(1, 30).isEmpty())

        val recipeAsFlow = getRecipe.execute(DUMMY_TOKEN, RECIPE_ID, true).toList()

        assert(recipeAsFlow[0].loading)

        val recipe = recipeAsFlow[1].data
        assert(recipe?.id == RECIPE_ID)

        assert(recipeDao.getRecipeById(RECIPE_ID)?.id == RECIPE_ID)

        assert(recipe is Recipe)
        assert(!recipeAsFlow[1].loading)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
