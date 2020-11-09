package com.example.children_health_card;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllergyListAdapter extends RecyclerView.Adapter<AllergyListAdapter.MyHolder> {

    Context context;
    List<AllergyListModel> allergyList;

    public AllergyListAdapter(Context context, List<AllergyListModel> allergyList) {
        this.context = context;
        this.allergyList = allergyList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wyświetlenie layoutu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allergy_element, parent, false);
        return  new AllergyListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllergyListAdapter.MyHolder myHolder, final int i) {

        //pobieranie danych do wyświetlenia na liście
        final String allergens = allergyList.get(i).getAllergens();
        final String symptoms = allergyList.get(i).getSymptoms();
        final String note = allergyList.get(i).getNote();
        final String name = allergyList.get(i).getNameChild();

        //ustawianie danych dziecka
        myHolder.Allergens.setText(allergens);
        myHolder.Symptoms.setText(symptoms);

        //wyświetlenie szczegółowych informacji u uczuleniu w momencie gdy zostanie kliknięte
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), AllergyInfoActivity.class);
                intent.putExtra("allergens",allergens);
                intent.putExtra("symptoms",symptoms);
                intent.putExtra("note",note);
                intent.putExtra("nameChild",name);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allergyList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView Allergens, Symptoms;
        LinearLayout linearLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            linearLayout = itemView.findViewById(R.id.p_linlay);
            Allergens = itemView.findViewById(R.id.allergensTv);
            Symptoms = itemView.findViewById(R.id.symptomsTv);
        }

    }
}