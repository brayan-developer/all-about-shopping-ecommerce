package ProductMicroservice.constants;

public class CacheConstants {
    public static final int CACHE_TTL_MINUTES = 10;

    public static final String GET_PRODUCT_BY_ID = "getProductById";

    public static final String GET_PRODUCTS_BY_IDS = "getProductsByIds";

    public static final String GET_PRODUCTS_BY_CATEGORY_BRAND_PRICE = "findProductsByCategoryBrandAndPriceRange";

    public static final String GET_PRICE_RANGE_BY_CATEGORY_BRAND = "getPriceRangeByCategoryAndBrand";


    public static final String GET_RANDOM_PRODUCTS_BY_CATEGORY = "getRandomProductsByCategory";

    public static final String GET_PRODUCTS_BY_SEARCH_TERM = "getProductsBySearchTerm";

    public static final String GET_PRICE_RANGE_BY_SEARCH_TERM = "getPriceRangeBySearchTerm";



    public static final String GET_ALL_CATEGORIES = "getAllCategories";
    public static final String GET_BRANDS_BY_CATEGORY = "getBrandsByCategory";


    public static final String GET_COVER_IMAGES_BY_GROUP = "getCoverImagesByGroup";


    public static final String GET_VARIANT_BY_ID = "getVariantById";
    public static final String GET_VARIANTS_BY_IDS = "getVariantsByIds";

    public static final String GET_VARIANTS_BY_PRODUCT = "getVariantsByProduct";

}
