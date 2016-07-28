package com.example.tacademy.jsonproject;

import com.google.gson.Gson;

/**
 * Created by Tacademy on 2016-07-28.
 */
public class ProductListJsonParser {
    private static final String TAG = "MainActivity";
    public static ProductList parse(String object){
        Gson gson = new Gson();
        ProductList productList = gson.fromJson(object, ProductList.class);
        return productList;

//        ProductList productList = new ProductList();

//        JSONObject jsonObject = null;
//        try{
//            jsonObject = new JSONObject(object);
//            productList.setStatus(jsonObject.getString("status"));
//            productList.setCount(jsonObject.getInt("count"));
//            JSONArray jsonArray = jsonObject.getJSONArray("list");
//            Product product = null;
//            JSONObject temp = null;
//            ArrayList<Product> list = new ArrayList<>();
//            for(int i = 0; i < jsonArray.length(); i++){
//                temp = jsonArray.getJSONObject(i);
//                product = new Product(temp.getString("name"), temp.getInt("price"), temp.getString("maker"));
//
//                list.add(product);
//            }
//            productList.setList(list);
//            Log.v(TAG, "json parser success");
//        }catch (JSONException e){
//            Log.v(TAG, "parser e : " + e);
//        }

//        return  productList;
    }
}
