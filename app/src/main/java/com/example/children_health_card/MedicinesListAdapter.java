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

public class MedicinesListAdapter extends RecyclerView.Adapter<MedicinesListAdapter.MyHolder> {

    Context context;
    List<MedicinesListModel> medicinesList;

    public MedicinesListAdapter(Context context, List<MedicinesListModel> medicinesList) {
        this.context = context;
        this.medicinesList = medicinesList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wyświetlenie layoutu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicines_element, parent, false);
        return  new MedicinesListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicinesListAdapter.MyHolder myHolder, final int i) {

        //pobieranie danych do wyświetlenia na liście
        final String medicineName = medicinesList.get(i).getMedicineName();
        final String dosage = medicinesList.get(i).getDosage();
        final String reason = medicinesList.get(i).getReason();
        final String nameChild = medicinesList.get(i).getNameChild();

        //ustawianie danych dziecka
        myHolder.MedicineName.setText(medicineName);
        myHolder.Dosage.setText(dosage);
        myHolder.Reason.setText(reason);

        //wyświetlenie szczegółowych informacji u uczuleniu w momencie gdy zostanie kliknięte
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), MedicinesInfoActivity.class);
                intent.putExtra("medicinename",medicineName);
                intent.putExtra("dosage",dosage);
                intent.putExtra("reason",reason);
                intent.putExtra("nameChild",nameChild);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicinesList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView MedicineName, Dosage, Reason;
        LinearLayout linearLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            linearLayout = itemView.findViewById(R.id.p_linlay);
            MedicineName = itemView.findViewById(R.id.medicineNameTv);
            Dosage = itemView.findViewById(R.id.dosageTv);
            Reason = itemView.findViewById(R.id.reasonTv);

        }

    }
}