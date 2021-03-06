package com.vsahin.moneycim.View.SpendingList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.vsahin.moneycim.Model.Entity.RawSpending;
import com.vsahin.moneycim.Model.Pojo.Spending;
import com.vsahin.moneycim.R;
import com.vsahin.moneycim.View.AddAndEditSpending.AddAndEditSpendingActivity;
import com.vsahin.moneycim.View.Base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Volkan Şahin on 17.08.2017.
 */

public class SpendingListFragment extends BaseFragment implements RecyclerViewItemClickListener {
    private final ArrayList<Spending> spendingList = new ArrayList<>();
    private SpendingListViewModel viewModel;
    private SpendingRecyclerViewAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private View view;

    @BindView(R.id.spending_recyclerview)
    GridRecyclerView spendingRecyclerView;

    public static SpendingListFragment newInstance() {
        return new SpendingListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_spending_list, container, false);
        ButterKnife.bind(this, view);

        adapter = new SpendingRecyclerViewAdapter(getActivity(), spendingList, this);

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        spendingRecyclerView.setAdapter(adapter);
        spendingRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SpendingListViewModel.class);
        subscribeSpendings();
    }

    private void subscribeSpendings() {
        viewModel.spendings.observe(this, new Observer<List<Spending>>() {
            @Override
            public void onChanged(final List<Spending> spendings) {
                adapter.updateItems(spendings);
            }
        });
    }

    @Override
    public void onItemClick(RawSpending clickedSpending) {
        startActivity(AddAndEditSpendingActivity.newIntent(getActivity(), clickedSpending));
    }

    @Override
    public void onItemLongClick(int longClickedSpendingId) {
        viewModel.deleteSpending(longClickedSpendingId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}
