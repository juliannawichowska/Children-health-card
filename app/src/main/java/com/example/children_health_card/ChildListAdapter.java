package com.example.children_health_card;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.MyHolder> {

    Context context;
    List<ChildListModel> childList;

    public ChildListAdapter(Context context, List<ChildListModel> childList) {
        this.context = context;
        this.childList = childList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wyświetlenie layoutu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_element, parent, false);
        return  new ChildListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildListAdapter.MyHolder myHolder, final int i) {

        //pobieranie danych do wyświetlenia na liście
        final String name = childList.get(i).getName();
        final String pesel = childList.get(i).getPesel();
        final String surname = childList.get(i).getSurname();
        String image = childList.get(i).getImageURL();

        //ustawianie danych dziecka
       myHolder.name.setText(name);
       myHolder.pesel.setText(pesel);

        //załadowanie zdjęcia profilowego z bazy danych
        try {
            Picasso.get().load(image).into(myHolder.photo);
        } catch (Exception e){
            Picasso.get().load(R.drawable.child).into(myHolder.photo);
        }

        //wyświetlenie profilu wybranego dziecka w momencie gdy zostanie kliknięty
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), HomeActivity.class);
                intent.putExtra("childName",name);
                intent.putExtra("childPesel",pesel);
                intent.putExtra("childSurname",surname);
               // Toast.makeText(context, "Kliknięto dziecko o imieniu " + name, Toast.LENGTH_LONG).show();
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView name, pesel;
        LinearLayout linearLayout;
        ImageView photo;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            linearLayout = itemView.findViewById(R.id.p_linlay);
            name = itemView.findViewById(R.id.nameTv);
            pesel = itemView.findViewById(R.id.peselTv);
            photo = itemView.findViewById(R.id.profilePicture);
        }

    }
}