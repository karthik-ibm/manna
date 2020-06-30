package com.ibm.manna.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibm.manna.R;
import com.ibm.manna.RecyclerTouchListener;
import com.ibm.manna.db_handler.Manna_DB_Handler;
import com.ibm.manna.need.CatalogAdapter;
import com.ibm.manna.need.NeedsItemsFragment;
import com.ibm.manna.pojo.AuthenticationResponse;
import com.ibm.manna.pojo.Catalog;

import java.util.ArrayList;

public class ShareFragment extends Fragment implements View.OnClickListener{

    RecyclerView recyclerViewShare;
    SearchView searchViewShare;
    ArrayList<Catalog> catalogArrayListShare;
    AuthenticationResponse response;

    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.shareToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("on back press 1");
            }
        });

        recyclerViewShare = view.findViewById(R.id.share_category_list);

        catalogArrayListShare = new ArrayList<>();
        Manna_DB_Handler manna_db_handler = new Manna_DB_Handler(getContext());
        catalogArrayListShare = manna_db_handler.getCatalog();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerViewShare.setLayoutManager(gridLayoutManager);
        CatalogAdapter adapter = new CatalogAdapter(getContext(),catalogArrayListShare);
        recyclerViewShare.setAdapter(adapter);


        recyclerViewShare.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerViewShare, new RecyclerTouchListener.ClickListener(){

            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("Catalog",catalogArrayListShare.get(position));
                loadCategoryItemFragment(bundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public void loadCategoryItemFragment(Bundle args)
    {
        Fragment fragment = new ShareItemsFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homefragment, fragment);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
    }
}
