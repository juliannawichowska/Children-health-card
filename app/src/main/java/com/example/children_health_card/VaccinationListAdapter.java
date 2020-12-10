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

public class VaccinationListAdapter extends RecyclerView.Adapter<VaccinationListAdapter.MyHolder> {

    Context context;
    List<VaccinationListModel> vaccinationList;

    public VaccinationListAdapter(Context context, List<VaccinationListModel> vaccinationList) {
        this.context = context;
        this.vaccinationList = vaccinationList;
    }

    @NonNull
    @Override
    public VaccinationListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wyświetlenie layoutu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccination_element, parent, false);
        return  new VaccinationListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccinationListAdapter.MyHolder myHolder, final int i) {

        //pobieranie danych do wyświetlenia na liście
        final String vaccinationType = vaccinationList.get(i).getVaccinationType();
        final String nameChild = vaccinationList.get(i).getNameChild();

        //ustawianie danych dziecka
        myHolder.VaccinationType.setText(vaccinationType);


        //wyświetlenie szczegółowych informacji u uczuleniu w momencie gdy zostanie kliknięte
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), VaccinationInfoActivity.class);
                intent.putExtra("vaccinationtype",vaccinationType);
                intent.putExtra("nameChild",nameChild);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vaccinationList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView VaccinationType;
        LinearLayout linearLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            linearLayout = itemView.findViewById(R.id.p_linlay);
            VaccinationType = itemView.findViewById(R.id.vaccinationTypeTv);

        }

    }
}
