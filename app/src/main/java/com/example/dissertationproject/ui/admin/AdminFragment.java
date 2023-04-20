/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.databinding.FragmentAdminBinding;
import com.example.dissertationproject.objects.User;

import java.util.ArrayList;


public class AdminFragment extends Fragment {

    RecyclerView recyclerView;
    SearchView searchView;
    private FragmentAdminBinding binding;

    /**
     * Method for when the view is created
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    /**
     * Method when the view has been created
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rcvAdminPanel);
        recyclerView.setHasFixedSize(true);
        searchView = view.findViewById(R.id.searchbar_admin);

        //Updates the view when the user types in the search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                updateView(s);
                return false;
            }
        });

        AdminPanelAdapter wpAdapter = new AdminPanelAdapter(getContext(), User.users);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(wpAdapter);
    }

    /**
     * When the view has been resumed, update the contents of the recycler view
     */
    @Override
    public void onResume() {
        super.onResume();
        AdminPanelAdapter wpAdapter = new AdminPanelAdapter(getContext(), User.users);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(wpAdapter);
    }

    /**
     * Updates the data of the recycler view based on what is being searched
     * @param search    the string that the user has entered to be searched
     */
    public void updateView(String search){
        ArrayList<User> users = new ArrayList<>();

        for(User u : User.users){
            //If the name of the user, or the email address of the user is similar to the search
            if(Utils.findSimilarity(u.getName(), search) > 0.3 || Utils.findSimilarity(u.getEmail(), search) > 0.1){
                users.add(u);
            }
        }

        AdminPanelAdapter wpAdapter = new AdminPanelAdapter(getContext(), users);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(wpAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}