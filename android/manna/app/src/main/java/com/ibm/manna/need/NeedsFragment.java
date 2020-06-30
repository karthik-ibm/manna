package com.ibm.manna.need;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibm.manna.MainActivity;
import com.ibm.manna.R;
import com.ibm.manna.RecyclerTouchListener;
import com.ibm.manna.db_handler.Manna_DB_Handler;
import com.ibm.manna.pojo.AuthenticationResponse;
import com.ibm.manna.pojo.Catalog;
import com.ibm.manna.pojo.CatalogItems;

import java.util.ArrayList;

public class NeedsFragment extends Fragment implements View.OnClickListener {


    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<Catalog> catalogArrayList;
    AuthenticationResponse response;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_needs, container, false);

        /*Bundle args = getArguments();
        if (args != null) {
            response = args.getParcelable("response");
            System.out.println("Share map fragment: "+ response.getAll_requests().size());
        }*/

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.needToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("on back press 1");
            }
        });

        recyclerView = view.findViewById(R.id.need_category_list);

        catalogArrayList = new ArrayList<>();
        Manna_DB_Handler manna_db_handler = new Manna_DB_Handler(getContext());
        catalogArrayList = manna_db_handler.getCatalog();
        /*String reqCat, String category, String cat_descr, ArrayList< CatalogItems > item

        catalogArrayList.add(0,new Catalog(R.drawable.vegtables,"Food","Vegtables"));
        catalogArrayList.add(1,new Catalog(R.drawable.fruits,"Fruits"));
        catalogArrayList.add(2,new Catalog(R.drawable.rice,"Rice"));
        catalogArrayList.add(3,new Catalog(R.drawable.pulses,"Pulses"));
        catalogArrayList.add(4,new Catalog(R.drawable.wheat,"Wheat"));
        catalogArrayList.add(5,new Catalog(R.drawable.groceries,"Groceries"));
        catalogArrayList.add(6,new Catalog(R.drawable.clothes,"Clothes"));
        catalogArrayList.add(7,new Catalog(R.drawable.books,"Books"));
        catalogArrayList.add(8,new Catalog(R.drawable.stationary,"Stationary"));*/


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);
        CatalogAdapter adapter = new CatalogAdapter(getContext(),catalogArrayList);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener(){

            @Override
            public void onClick(View view, int position) {
                System.out.println("Catalog detail : "+catalogArrayList.get(position).getCat_descr());
                Bundle bundle = new Bundle();
                bundle.putParcelable("Catalog",catalogArrayList.get(position));
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
        switch (v.getId())
        {
            case R.id.needToolbar:
                System.out.println("on back press");
                break;

        }

    }


    public void loadCategoryItemFragment(Bundle args)
    {
        Fragment fragment = new NeedsItemsFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homefragment, fragment);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
    }
}
