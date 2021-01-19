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

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyHolder> {

    Context context;
    List<ContactsModel> contactsList;

    public ContactsAdapter(Context context, List<ContactsModel> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
    }

    @NonNull
    public ContactsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wyświetlenie layoutu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_element, parent, false);
        return new ContactsAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.MyHolder myHolder, int i) {
        //pobieranie danych do wyświetlenia na liście
        final String doctorNumber = contactsList.get(i).getDoctorNumber();
        final String doctorType = contactsList.get(i).getDoctorType();
        final String doctorName = contactsList.get(i).getDoctorName();
        final String doctorAddress = contactsList.get(i).getDoctorAddress();

        //ustawianie danych dziecka
        myHolder.DoctorName.setText(doctorName);
        myHolder.DoctorType.setText(doctorType);

        //wyświetlenie szczegółowych informacji u uczuleniu w momencie gdy zostanie kliknięte
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), ContactsInfoActivity.class);
                intent.putExtra("doctorNumber",doctorNumber);
                intent.putExtra("doctorType",doctorType);
                intent.putExtra("doctorName",doctorName);
                intent.putExtra("doctorAddress",doctorAddress);
                v.getContext().startActivity(intent);
            }
        });

    }

    public int getItemCount() {
        return contactsList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView DoctorType, DoctorName;
        LinearLayout linearLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            linearLayout = itemView.findViewById(R.id.p_linlay);
            DoctorType = itemView.findViewById(R.id.contactsTypeTv);
            DoctorName = itemView.findViewById(R.id.contactsNameTv);
        }

    }


}
