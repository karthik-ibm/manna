package com.ibm.manna.need;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibm.manna.R;
import com.ibm.manna.cart.NeedCartFragment;
import com.ibm.manna.pojo.Cart;
import com.ibm.manna.pojo.Catalog;
import com.ibm.manna.pojo.CatalogItems;
import com.ibm.manna.pojo.RequestItems;
import com.ibm.manna.pojo.Requests;

import java.util.ArrayList;

public class NeedsItemsFragment extends Fragment {
    RecyclerView recyclerView;
    SearchView searchView;
    Catalog catalog;
    ImageView categoryImage;
    TextView categoryDescr,needsQuantity;
    Button tapButton;

    CatalogItemAdapter catalogItemAdapter;
    ArrayList<CatalogItems> catalogItemsArrayList;
    Requests requests;
    Cart cart;
    View view;

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
       // catalog = new Catalog();
        if (args != null) {
            catalog = args.getParcelable("Catalog");
            //System.out.println(catalog.getDescr());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_needs_items, container, false);

        //catalog = new Catalog();
        Bundle args = getArguments();
        if (args != null) {
            catalog = args.getParcelable("Catalog");
        }

        cart = new Cart();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.needToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("on back press 12");
            }
        });

        needsQuantity = view.findViewById(R.id.need_quantity);
        categoryImage = view.findViewById(R.id.catalog_image);
        categoryDescr= view.findViewById(R.id.catalog_descr);
        recyclerView = view.findViewById(R.id.need_subcategory_list);
        tapButton = view.findViewById(R.id.tapButton);


        //categoryImage.setImageResource(catalog.getImage());
        categoryImage.setImageResource(getContext().getResources().getIdentifier("drawable/"+catalog.getImage()+"_hdr", null, getContext().getPackageName()));
        categoryDescr.setText(catalog.getCat_descr());
        catalogItemsArrayList = new ArrayList<>();
        catalogItemsArrayList= catalog.getItem();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        catalogItemAdapter = new CatalogItemAdapter(getContext(),catalogItemsArrayList, new CatalogItemAdapter.DetailsAdapterListener() {
            @Override
            public void addOnClick(View v, int position) {

                //System.out.println("item "+catalogItemsArrayList.get(position).getItems());
                addItems(catalogItemsArrayList, position);
            }

            @Override
            public void subOnClick(View v, int position) {
                removeItems(catalogItemsArrayList, position);
            }
        });
        recyclerView.setAdapter(catalogItemAdapter);

        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<RequestItems> requestItemsArrayList = new ArrayList<>();
                for(int i = 0;i<cart.getCatalogItemsArrayList().size();i++)
                {

                    if(cart.getCatalogItemsArrayList().get(i).getQuantity()>0)
                    {
                        requestItemsArrayList.add(new RequestItems(catalog.getCategory(),catalog.getCat_descr(),cart.getCatalogItemsArrayList().get(i).getItem_id(),cart.getCatalogItemsArrayList().get(i).getItem_descr(),cart.getCatalogItemsArrayList().get(i).getMetric(),cart.getCatalogItemsArrayList().get(i).getImage(),0,(int)cart.getCatalogItemsArrayList().get(i).getQuantity(),0));
                    }
                }
                Bundle bundle = new Bundle();
                Requests requests = new Requests();
                requests.setReqType("S");
                requests.setReqCat(catalog.getReqCat());
                requests.setReqItems(requestItemsArrayList );
                cart.setRequests(requests);

                bundle.putParcelable("request",cart);
                loadCartFragment(bundle);
            }
        });

        return view;
    }
    public void addItems(ArrayList<CatalogItems> catalogItemsArrayList, int position)
    {

        if(cart.getCatalogItemsArrayList().indexOf(catalogItemsArrayList.get(position))>=0)
        {
            int index = cart.getCatalogItemsArrayList().indexOf(catalogItemsArrayList.get(position));
            cart.getCatalogItemsArrayList().get(index).setQuantity(cart.getCatalogItemsArrayList().get(index).getQuantity() + 1);
            cart.setCount(cart.getCount()+1);

        }
        else
        {
            catalogItemsArrayList.get(position).setQuantity(catalogItemsArrayList.get(position).getQuantity() +1);
            ArrayList<CatalogItems> tmp = cart.getCatalogItemsArrayList();
            tmp.add(catalogItemsArrayList.get(position));
            cart.setCatalogItemsArrayList(tmp);
            cart.setCount(cart.getCount()+1);

        }
        needsQuantity.setText(String.valueOf(cart.getCount()));
        catalogItemAdapter.notifyDataSetChanged();
    }

    public void removeItems(ArrayList<CatalogItems> catalogItemsArrayList, int position)
    {
        if(cart.getCatalogItemsArrayList().indexOf(catalogItemsArrayList.get(position))>=0)
        {
            if(catalogItemsArrayList.get(position).getQuantity()>0) {
                int index = cart.getCatalogItemsArrayList().indexOf(catalogItemsArrayList.get(position));
                cart.getCatalogItemsArrayList().get(index).setQuantity(cart.getCatalogItemsArrayList().get(index).getQuantity() - 1);
                if (cart.getCount() > 0)
                    cart.setCount(cart.getCount() - 1);
            }
        }
        else
        {
            if(catalogItemsArrayList.get(position).getQuantity()>0) {
                catalogItemsArrayList.get(position).setQuantity(catalogItemsArrayList.get(position).getQuantity() - 1);
                ArrayList<CatalogItems> tmp = cart.getCatalogItemsArrayList();
                tmp.add(catalogItemsArrayList.get(position));
                cart.setCatalogItemsArrayList(tmp);
                if (cart.getCount() > 0)
                    cart.setCount(cart.getCount() - 1);
            }

        }
        catalogItemAdapter.notifyDataSetChanged();
        needsQuantity.setText(String.valueOf(cart.getCount()));
    }

    public void loadCartFragment(Bundle args)
    {
        Fragment fragment = new NeedCartFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homefragment, fragment);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
    }

}
