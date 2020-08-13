package com.rosa;

import android.app.Application;
import android.content.Context;
import ir.map.sdk_map.Mapir;


public class ContextCamp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        ContextCamp.context = getApplicationContext();
        Mapir.getInstance(this, "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjlhNGViMGI2YTIwMTM4MDgyODhhNDIwMDE1NTU0MTkyMjk1ODYxNzcxNjBhYzA4MjVhMjRjZDRiNzU4YjZiYTZhNGI1MDgyMjZiZDcyMjc1In0.eyJhdWQiOiIxMDQ0MCIsImp0aSI6IjlhNGViMGI2YTIwMTM4MDgyODhhNDIwMDE1NTU0MTkyMjk1ODYxNzcxNjBhYzA4MjVhMjRjZDRiNzU4YjZiYTZhNGI1MDgyMjZiZDcyMjc1IiwiaWF0IjoxNTk3MjIyMTUyLCJuYmYiOjE1OTcyMjIxNTIsImV4cCI6MTU5OTgxNDE1Miwic3ViIjoiIiwic2NvcGVzIjpbImJhc2ljIl19.Nfn64u4gqwDflVr_oE-jLdgO_Z0lWXOXg9fEsC6chgUmrDecMcUTUs7mW5yY_tuyUkNZyyXtM3QM2Cg2hgTCmhwPmTKqcb5_LexRd7bSbjr7ZF_heNdbIeM6Dv5Yc8j2MkxXgd9gaA0doBbxoM0du8tBZpa3hlkMSX-F1LqCveJ33UfyC_g1Tfprr1p7wp6wADhqOUVdYFTVfTjLgDeoJqSWsA9V379Ok1LRP8JGgTYxEC4NlvLlnLOumBucHyT_G5E2duHya869i1lbuFMrWSTpiVN6G_gspKFMwtTScSK5cMRdGkdc_asw9-noqM3pZq6Cj0YyoK5eTd1rjLh8ng");

    }

    public static Context getAppContext() {
        return ContextCamp.context;
    }



}


