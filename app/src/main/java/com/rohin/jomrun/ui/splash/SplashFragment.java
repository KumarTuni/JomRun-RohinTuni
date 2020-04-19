package com.rohin.jomrun.ui.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohin.jomrun.R;
import com.rohin.jomrun.databinding.FragmentSplashBinding;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class SplashFragment extends Fragment {

    private Timer timer;
    private TimerTask timerTask;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSplashBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(() -> navController.navigate(SplashFragmentDirections.actionSplashFragmentToNavigationHome()));
            }
        };
        timer.schedule(timerTask, 2500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerTask.cancel();
        timer.purge();
    }
}
