package com.example.children_health_card;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OperationsListAdapter extends RecyclerView.Adapter<OperationsListAdapter.MyHolder> {

    Context context;
    List<OperationsListModel> operationsList;

    public OperationsListAdapter(Context context, List<OperationsListModel> operationsList) {
        this.context = context;
        this.operationsList = operationsList;
    }

    @NonNull
    public OperationsListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wyświetlenie layoutu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.operations_element, parent, false);
        return new OperationsListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OperationsListAdapter.MyHolder myHolder, int i) {
        //pobieranie danych do wyświetlenia na liście
        final String date = operationsList.get(i).getDate();
        final String doctor = operationsList.get(i).getDoctor();
        final String note = operationsList.get(i).getNote();
        final String nameChild = operationsList.get(i).getNameChild();

        //ustawianie danych dziecka
        myHolder.OperationsNote.setText(note);
        myHolder.OperationsDate.setText(date);

        //wyświetlenie szczegółowych informacji u uczuleniu w momencie gdy zostanie kliknięte
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), OperationsInfoActivity.class);
                intent.putExtra("doctor",doctor);
                intent.putExtra("date",date);
                intent.putExtra("note",note);
                intent.putExtra("nameChild",nameChild);
                v.getContext().startActivity(intent);
            }
        });

    }

    public int getItemCount() {
        return operationsList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView OperationsNote, OperationsDate;
        LinearLayout linearLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            linearLayout = itemView.findViewById(R.id.p_linlay);
            OperationsNote = itemView.findViewById(R.id.operationNoteTv);
            OperationsDate = itemView.findViewById(R.id.operationDateTv);
        }

    }


}
