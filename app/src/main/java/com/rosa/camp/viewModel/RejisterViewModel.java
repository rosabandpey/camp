package com.rosa.camp.viewModel;

import android.util.Log;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.JsonObject;
import com.rosa.camp.BR;
import com.rosa.camp.model.User;
import com.rosa.camp.repository.remote.ApiClient;
import com.rosa.camp.repository.remote.UserInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RejisterViewModel extends BaseObservable {

    public User registerUser;
    private String successMessage = "Register has completed";
    private String errorMessage = "not Registered";
    private String successMessageEmail="Email exists";
    private String errorMessageEmail="Email  does not exist";
    ApiClient apiClient = new ApiClient();
    Retrofit retrofit = apiClient.getRetrofitInstance();
    UserInterface userInterface = retrofit.create(UserInterface.class);
    private String userName, userFamily, userPhone, userEmail, userPassword;

    @Bindable
    private String toastMessage = null;

    public RejisterViewModel() {

        registerUser = new User("", "", "", "", "","","");
        apiClient = new ApiClient();
        retrofit = apiClient.getRetrofitInstance();
        userInterface = retrofit.create(UserInterface.class);
    }

    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    @Bindable
    public String getUserName() {
        return registerUser.getName();
    }

    public void setUserName(String userName) {
        registerUser.setName(userName);
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getUserFamily() {
        return registerUser.getFamily();
    }

    public void setUserFamily(String userFamily) {
        registerUser.setFamily(userFamily);
        notifyPropertyChanged(BR.family);
    }

    @Bindable
    public String getUserPhone() {
        return registerUser.getPhone();

    }

    public void setUserPhone(String userPhone) {
        registerUser.setPhone(userPhone);
        notifyPropertyChanged(BR.userPhone);
    }

    @Bindable
    public String getUserEmail() {
        return registerUser.getEmail();
    }

    public void setUserEmail(String userEmail) {
        registerUser.setEmail(userEmail);
        notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getUserPassword() {
        return registerUser.getPassword();
    }

    public void setUserPassword(String userPassword) {
        registerUser.setPassword(userPassword);
        notifyPropertyChanged(BR.userPassword);

    }


    public void onClick(View view) {

        Call<JsonObject> call = userInterface.RegisterUser(userName, userFamily, userPhone, userEmail, userPassword);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    String code = String.valueOf(response.code());
                    Log.d("code", code);
                    return;
                }
                JsonObject jsonObject = response.body();
                Boolean methodrespose = jsonObject.get("Result").getAsBoolean();
                if (methodrespose) {
                    setToastMessage(successMessage);
                } else {
                    setToastMessage(errorMessage);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("error", t.getMessage());
                setToastMessage(errorMessage);

            }
        });


    }

  /*  public View.OnFocusChangeListener getOnFocusChangeListener(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocussed) {

                Log.i("focus","focus change");
                Call<JsonObject> call = userInterface.IsUSerExists(userEmail);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (!response.isSuccessful()) {
                            String code = String.valueOf(response.code());
                            String message=response.message();
                            Log.d("code", code);
                            Log.d("message", message);
                            return;
                        }
                        JsonObject jsonObject = response.body();
                        Boolean methodResponse = jsonObject.get("Result").getAsBoolean();
                        if (methodResponse) {
                            setToastMessage(successMessageEmail);
                        } else {
                            setToastMessage(errorMessageEmail);
                        }

                    }

                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.i("error", t.getMessage());
                        setToastMessage(errorMessage);
                    }
                });
            }
            };
        } */

}