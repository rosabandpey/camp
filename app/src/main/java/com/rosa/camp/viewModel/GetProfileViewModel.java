package com.rosa.camp.viewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.DataBinderMapperImpl;

import com.google.gson.JsonObject;
import com.rosa.camp.BR;
import com.rosa.camp.model.GetProfile;
import com.rosa.camp.model.LoginUser;
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

public class GetProfileViewModel extends BaseObservable {
    User user;
    PreferenceHelper preferenceHelper;
    ApiClient apiClient;
    Retrofit retrofit;
    UserInterface userInterface;
    private String name;
    private  String family;
    private  String phone;
    private  String email;
    Context context;

    public GetProfileViewModel()
    {
        user=new User("","","","","","","");
        apiClient=new ApiClient();
        retrofit=apiClient.getRetrofitInstance();
        userInterface=retrofit.create(UserInterface.class);
        context=com.rosa.ContextCamp.getAppContext();
        loadProfile(context);
    }


    public void loadProfile(Context con){
        preferenceHelper =new PreferenceHelper(con);

                setName(preferenceHelper.getName());
                setFamily(preferenceHelper.getFamily());
                setPhone(preferenceHelper.getPhone());

            }

    @Bindable
    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
        notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getName() {
        return user.getName();
    }

    public void setName(String name) {
        user.setName(name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getFamily() {
        return user.getFamily();
    }

    public void setFamily(String family) {
        user.setFamily(family);
        notifyPropertyChanged(BR.family);
    }

    @Bindable
    public String getPhone() {
        return user.getPhone();
    }

    public void setPhone(String phone) {
        user.setPhone(phone);
        notifyPropertyChanged(BR.phone);
    }


    public void onClick(View view){

        context = view.getContext();
        Intent intent = new Intent(context, UpdateProfile.class);
        context.startActivity(intent);



    }

}
