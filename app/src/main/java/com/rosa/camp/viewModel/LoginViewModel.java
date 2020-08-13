package com.rosa.camp.viewModel;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.rosa.camp.BR;
import com.rosa.camp.model.User;

import com.rosa.camp.repository.remote.ApiClient;
import com.rosa.camp.repository.remote.PreferenceHelper;
import com.rosa.camp.repository.remote.UserInterface;
import com.rosa.camp.view.GetProfileActivity;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginViewModel extends ViewModel {




    private  String family;
    private  String phone;


    public static User loginUser;

    ApiClient apiClient;
    Retrofit retrofit;
    static UserInterface userInterface;




    public LoginViewModel (){
        loginUser=new User("","","","","","","");
        apiClient=new ApiClient();
        retrofit=apiClient.getRetrofitInstance();
        userInterface=retrofit.create(UserInterface.class);


    }

    public static class Observer extends BaseObservable {

        @Bindable
        private String toastMessage = null;
        private String email;
        private String password;
        private String name;
        private String successMessage = "Login was successful";
        private String errorMessage = "Email or Password not valid";
        private String successMessageApi = "Successful";
        private String errorMessageApi = "Not Successful";
        Context context;
        PreferenceHelper preferenceHelper;


        public String getToastMessage() {
            return toastMessage;
        }


        private void setToastMessage(String toastMessage) {

            this.toastMessage = toastMessage;
            notifyPropertyChanged(BR.toastMessage);
        }

        @Bindable
        public String getUserName() {
            return loginUser.getName();
        }


        @Bindable
        public String getUserFamily() {
            return loginUser.getFamily();
        }


        @Bindable
        public String getUserPhone() {
            return loginUser.getPhone();

        }


        @Bindable
        public String getUserEmail() {
            return loginUser.getEmail();
        }

        public void setUserEmail(String email) {
            loginUser.setEmail(email);
            notifyPropertyChanged(BR.userEmail);
        }

        @Bindable
        public String getUserPassword() {
            return loginUser.getPassword();
        }

        public void setUserPassword(String password) {
            loginUser.setPassword(password);
            notifyPropertyChanged(BR.userPassword);
        }

        public void onClick(final View view) {

            if (isInputDataValid()) {
                Call<JsonObject> call = userInterface.LoginUser(email, password);
                setToastMessage(successMessage);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.i("Responsestring :", response.body().toString());
                        if (!response.isSuccessful()) {
                            String code = String.valueOf(response.code());
                            Log.d("code", code);
                            return;
                        }

                        if (response.body() != null) {

                            Log.i("onSuccess", response.body().toString());

                            JsonObject object = response.body();
                            Boolean success = object.get("Result").getAsBoolean();
                            if (success) {
                                setToastMessage(successMessageApi);
                                context = view.getContext();
                                loadProfile();
                                Intent intent = new Intent(context, GetProfileActivity.class);
                                context.startActivity(intent);

                            }

                        } else {
                            setToastMessage(errorMessageApi);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.i("error", t.getMessage());
                        setToastMessage(errorMessageApi);
                    }
                });
            } else
                setToastMessage(errorMessage);
        }


        public boolean isInputDataValid() {
            return !TextUtils.isEmpty(getUserEmail()) && Patterns.EMAIL_ADDRESS.matcher(getUserEmail()).matches() && getUserPassword().length() > 5;
        }

        public void loadProfile() {
            preferenceHelper = new PreferenceHelper(context);
            Call<JsonObject> call = userInterface.GetProfile(email);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (!response.isSuccessful()) {
                        String code = String.valueOf(response.code());
                        Log.d("code", code);
                        return;
                    }

                    JsonObject jsonObject = response.body();
                    Log.i("name", jsonObject.get("Name").getAsString());
                    name = jsonObject.get("Name").getAsString();

                    preferenceHelper.putNAME(jsonObject.get("Name").getAsString());
                    preferenceHelper.putFamily(jsonObject.get("Family").getAsString());
                    preferenceHelper.putPhone(jsonObject.get("Phone").getAsString());

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.i("error", t.getMessage());
                    // setToastMessage(errorMessage);

                }
            });
        }


    }
}
