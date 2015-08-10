package pl.javastart.wydatex.expense;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.javastart.wydatex.R;
import pl.javastart.wydatex.database.Expense;
import pl.javastart.wydatex.database.ExpenseRepository;

public class ExpenseListFragment extends Fragment {

    private ExpenseAdapter expenseListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        setupRecyclerView(view);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ExpenseActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private void setupRecyclerView(View parent) {
        RecyclerView recyclerView = (RecyclerView) parent.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        List<Expense> expenses = ExpenseRepository.getAllExpenses(getActivity());

        expenseListAdapter = new ExpenseAdapter(getActivity(), expenses);
        recyclerView.setAdapter(expenseListAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        refreshAdapterData();
    }

    private void refreshAdapterData() {
        List<Expense> expenses = ExpenseRepository.getAllExpenses(getActivity());
        expenseListAdapter.setExpenses(expenses);
        expenseListAdapter.notifyDataSetChanged();
    }
}
