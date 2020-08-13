package com.rosa.camp.viewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.JsonObject;
import com.rosa.ContextCamp;
import com.rosa.camp.model.User;
import com.rosa.camp.repository.remote.ApiClient;
import com.rosa.camp.repository.remote.PreferenceHelper;
import com.rosa.camp.repository.remote.UserInterface;
import com.rosa.camp.view.GetProfileActivity;
import com.rosa.camp.view.UpdateProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateProfileViewModel extends BaseObservable {

    private String userName;
    private String userFamily;
    private String userEmail;
    private String userSex;
    private String userPhone;
    private String userBirthDate;
    public PreferenceHelper preferenceHelper;
    public UserInterface userInterface;
    public User updateUser;
    ApiClient apiClient;
    Retrofit retrofit;
    Context context;
    private String successMessage = "Register has completed";
    private String errorMessage = "not Registered";

    @Bindable
    private String toastMessage = null;

    public UpdateProfileViewModel() {

        updateUser = new User("", "", "", "", "","","");
        context= ContextCamp.getAppContext();
        apiClient = new ApiClient();
        retrofit = apiClient.getRetrofitInstance();
        userInterface = retrofit.create(UserInterface.class);
    }

    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(com.rosa.camp.BR.toastMessage);
    }

    @Bindable
    public String getUserName() {
        return updateUser.getName();
    }

    public void setUserName(String name) {
        updateUser.setName(name);
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getUserFamily() {
        return updateUser.getFamily();
    }

    public void setUserFamily(String family) {
        updateUser.setFamily(family);
        notifyPropertyChanged(BR.userFamily);
    }

    @Bindable
    public String getUserEmail() {
        return updateUser.getEmail();
    }


    public void setUserEmail(String email) {
        updateUser.setEmail(email);
        notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getUserSex() {
        return updateUser.getSex();
    }

    public void setUserSex(String sex) {
       updateUser.setSex(sex);
       notifyPropertyChanged(com.rosa.camp.BR.userSex);
    }

    @Bindable
    public String getUserPhone() {
        return updateUser.getPhone();
    }

    public void setUserPhone(String phone) {
        updateUser.setPhone(phone);
        notifyPropertyChanged(BR.userPhone);
    }

    @Bindable
    public String getUserBirthDate() {
        return updateUser.getBirthDate();
    }

    public void setUserBirthDate(String birthDate) {
        updateUser.setBirthDate(birthDate);
        notifyPropertyChanged(com.rosa.camp.BR.userBirthDate);
    }

    public void onClick(View view) {

        Call<JsonObject> call = userInterface.UpdateProfile(userName,userFamily,userPhone,userSex,userBirthDate,userEmail);
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
                    Log.i("succeed", "succeed");
                    setToastMessage(successMessage);
                    updateProfile(context);
                    context = view.getContext();
                    Intent intent = new Intent(context, GetProfileActivity.class);
                    context.startActivity(intent);


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

    public void updateProfile(Context context) {
                preferenceHelper = new PreferenceHelper(context);
                preferenceHelper.putNAME(getUserName());
                preferenceHelper.putFamily(getUserFamily());
                preferenceHelper.putEmail(getUserEmail());
                preferenceHelper.putPhone(getUserPhone());
                preferenceHelper.putBirth(getUserBirthDate());
                preferenceHelper.putSex(getUserSex());

                Log.i("PreferenceHelper", getUserPhone());
            }


}
