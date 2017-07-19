package com.example.gabbygiordano.marketplace;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by tanvigupta on 7/11/17.
 */

public class MarketPlaceClient {
    public static final String GET_URL = "https://api.data.gov/ed/collegescorecard";
    public static final String ENDPOINT = "/v1/schools";
    public static final String FORMAT = ".json?";

    public static final String API_KEY = "api_key=1PVcYE59lhrYLGtQN2FhXppctGHkHE7KvFYgYcPu";

    public static final String OPTION_PARAMS = "&school.region_id__not=9" +
                                               "&school.carnegie_undergrad__range=5..15" +
                                               "&school.carnegie_size_setting__range=6..17" +
                                               "&school.online_only=0" +
                                               "&school.institutional_characteristics.level=1" +
                                               "&school.main_campus=1" +
                                               "&school.degrees_awarded.highest__range=3..4" +
                                               "&school.locale__range=11..33" +
                                               "&_fields=school.name" +
                                               "&_sort=school.name";

    public static String PAGE_OPTIONS;

    public static void getSchoolList(int page, AsyncHttpResponseHandler handler) {
        PAGE_OPTIONS = "&_page=" + page;
        String url = GET_URL + ENDPOINT + FORMAT + API_KEY + OPTION_PARAMS + PAGE_OPTIONS;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, handler);
    }


}
