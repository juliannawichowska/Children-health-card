package com.example.children_health_card;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CalendarFragment extends Fragment {
    private static Context context = null;
    private CalendarView mCalendarView;
    private ExpandableListView expandableListView;
    CustomListAdapter expandableadapter;
    ArrayList<String> headersList;
    private HashMap<String, ArrayList<String>> itemsMap = new HashMap<String, ArrayList<String>>();
    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //My Implimentation
    List<String> tasks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        super.onCreate(savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Children");

        mCalendarView = (CalendarView) v.findViewById(R.id.calendarView);
        expandableListView = (ExpandableListView) v.findViewById(R.id.listCalendar);


        //Now Saving Tasks(from firebase nodes:task1,task2,task3) to ArrayList and then adding data to HashMap
        itemsMap = new HashMap<String, ArrayList<String>>();
        headersList = new ArrayList<>();
        tasks = new ArrayList<>();


        // get expandable list adapter
        expandableadapter = new CustomListAdapter(context, itemsMap, headersList);
        //set list adapter to list
        expandableListView.setAdapter(expandableadapter);

        //Refreshing Adapter
        expandableadapter.notifyDataSetChanged();


        // Toast item title when ListView item is clicked
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView listview, View child, int groupposition, int childposition, long position) {
                Toast.makeText(context.getApplicationContext(), itemsMap.get(headersList.get(groupposition)).get(childposition), Toast.LENGTH_LONG).show();
                return true;
            }
        });


        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // make 2020-05-17 12:31:57

                String choosen_date;
                if (month == 10 || month == 11) {
                    if (dayOfMonth < 10) {
                        choosen_date = year + "-" + (month + 1) + "-0" + dayOfMonth;
                    } else {
                        choosen_date = year + "-" + (month + 1) + "-" + dayOfMonth;
                    }
                    Log.e("TAG", choosen_date);
                } else {
                    if (dayOfMonth < 10) {
                        choosen_date = year + "-0" + (month + 1) + "-0" + dayOfMonth;
                    } else {
                        choosen_date = year + "-0" + (month + 1) + "-" + dayOfMonth;
                    }
                    Log.e("TAG", choosen_date);
                }
                //Getting data from firebase
                getDataFromFirebase();
            }
        });

        return v;
    }

    private void getDataFromFirebase() {
//Firebase Read Listener
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Running Foreach loop
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    //Getting Value from 1 to 10 in ArrayList(tasks)
                    tasks.add(d.getValue().toString());
                }

                //Putting key & tasks(ArrayList) in HashMap
                itemsMap.put(dataSnapshot.getKey(), (ArrayList<String>) tasks);

                headersList.add(dataSnapshot.getKey());

                tasks = new ArrayList<>();

                Log.d(TAG, "onChildAdded: dataSnapshot.getChildren: " + dataSnapshot.getChildren());
                Log.d(TAG, "onChildAdded: KEY" + dataSnapshot.getKey() + " value " + dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: " + dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //task.remove("" + dataSnapshot.getValue());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


   /* private class CustomListAdapter extends BaseExpandableListAdapter {

        private Context con;
        HashMap<String, ArrayList<String>> headeritems;
        ArrayList<String> headers;

        // Public constructor
        public CustomListAdapter(Context context, HashMap<String, ArrayList<String>>listheaders , ArrayList<String>headerchilds ) {
            this.con = context;
            this.headeritems = listheaders;
            this.headers = headerchilds;
        }

        @Override
        public int getGroupCount() {
            // Return total number of groups
            return headers.size();
        }

        @Override
        public int getChildrenCount(int position) {
            // Return total items in each group
            return headeritems.get(headers.get(position)).size();
        }

        @Override
        public Object getGroup(int position) {
            // Return group heading
            return headers.get(position);
        }

        @Override
        public Object getChild(int groupposition, int childposition) {
            // Return child from specified group at the specified position
            return headeritems.get(headers.get(groupposition)).get(childposition);
        }

        @Override
        public long getGroupId(int position) {
            return position;
        }

        @Override
        public long getChildId(int groupposition, int childposition) {
            return childposition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
            View v = convertView;
            // Inflate the layout for each list row
            LayoutInflater _inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (v == null) {
                LayoutInflater inflater=LayoutInflater.from(con);
                v = _inflater.inflate(R.layout.calendar_header, null);
            }
            // Set Width of ListView to MATCH_PARENT
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // Get the TextView from CustomView for displaying group headings
            TextView txtview = (TextView) v.findViewById(R.id.listitemCalendar);
            // Set the text and image for current item using data from map list
            txtview.setText(headers.get(groupPosition));
            // Return the view for the current row
            return v;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
            View v = convertView;
            // Inflate the layout for each list row
            LayoutInflater _inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (v == null) {
                v = _inflater.inflate(R.layout.calendar_child, null);
            }
            // Set Width of ListView to MATCH_PARENT
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // Get the TextView from CustomView for displaying children
            TextView txtview = (TextView) v.findViewById(R.id.childitemCalendar);
            // Set the text for current item using data from map list
            txtview.setText((headeritems.get(headers.get(groupPosition))).get(childPosition));
            // Return the view for the current row
            return v;
        }

        @Override
        public boolean isChildSelectable(int groupposition, int childposition)
        {
            return true;
        }

    }*/

    public class CustomListAdapter extends BaseExpandableListAdapter {
        private Context con;
        HashMap<String, ArrayList<String>> headeritems;
        ArrayList<String> headers;

        public CustomListAdapter(Context context, HashMap<String, ArrayList<String>>listheaders , ArrayList<String>headerchilds) {
            this.con = context;
            this.headeritems = listheaders;
            this.headers = headerchilds;
        }

        // get child of header
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.headeritems.get(this.headers.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        // return the view of child
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String childname = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(con);
                convertView = inflater.inflate(R.layout.calendar_child, null);
            }
            TextView listchild = (TextView) convertView.findViewById(R.id.childitemCalendar);
            listchild.setText(childname);
            return convertView;
        }

        // returns children count of header
        @Override
        public int getChildrenCount(int groupPosition) {
            return this.headeritems.get(headers.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.headers.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.headers.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //returns the view of group
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headername = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(con);
                convertView = inflater.inflate(R.layout.calendar_header, null);
            }
            TextView header = (TextView) convertView.findViewById(R.id.listitemCalendar);
            header.setText(headername);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}