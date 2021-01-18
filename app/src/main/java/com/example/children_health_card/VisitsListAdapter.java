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

public class VisitsListAdapter extends RecyclerView.Adapter<VisitsListAdapter.MyHolder> {

    Context context;
    List<VisitsListModel> visitsList;

    public VisitsListAdapter(Context context, List<VisitsListModel> visitsList) {
        this.context = context;
        this.visitsList = visitsList;
    }

    @NonNull
    public VisitsListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wyświetlenie layoutu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visits_element, parent, false);
        return  new VisitsListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitsListAdapter.MyHolder myHolder, int i) {
        //pobieranie danych do wyświetlenia na liście
        final String date = visitsList.get(i).getDate();
        final String name = visitsList.get(i).getName();
        final String note = visitsList.get(i).getNote();
        final String type = visitsList.get(i).getType();
        final String nameChild = visitsList.get(i).getNameChild();

        //ustawianie danych dziecka
        myHolder.VisitName.setText(name);
        myHolder.VisitDate.setText(date);

        //wyświetlenie szczegółowych informacji u uczuleniu w momencie gdy zostanie kliknięte
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), VisitsInfoActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("date",date);
                intent.putExtra("note",note);
                intent.putExtra("nameChild",nameChild);
                intent.putExtra("type",type);
                v.getContext().startActivity(intent);
            }
        });

    }

    public int getItemCount() {
        return visitsList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView VisitName, VisitDate;
        LinearLayout linearLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            linearLayout = itemView.findViewById(R.id.p_linlay);
            VisitName = itemView.findViewById(R.id.visitNameTv);
            VisitDate = itemView.findViewById(R.id.visitDateTv);
        }

    }


}
