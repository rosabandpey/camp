package com.rosa.camp.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rosa.camp.R;
import com.rosa.camp.databinding.FragmentLoginBinding;
import com.rosa.camp.model.User;
import com.rosa.camp.viewModel.LoginViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentLoginBinding dataBiding;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      //  super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_login, container, false);
       // FragmentLoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
       // LoginViewModel model = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
       // binding.setLifecycleOwner(getActivity());
       // binding.setViewmodelclass(model);


        //Your codes here

        super.onCreateView(inflater, container, savedInstanceState);
        LoginViewModel.Observer observer=new LoginViewModel.Observer();
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        dataBiding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, null, false);
        dataBiding.setLifecycleOwner(getActivity());
        dataBiding.setLoginViewModel(observer);

        return dataBiding.getRoot();

    }
}