package co.edu.udea.telepeaje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.edu.udea.telepeaje.Objetos.Auto;
import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.Pago;
import co.edu.udea.telepeaje.Objetos.Recibo;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistorialPagosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistorialPagosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialPagosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewGroup layout;

    private OnFragmentInteractionListener mListener;

    public HistorialPagosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistorialPagosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistorialPagosFragment newInstance(String param1, String param2) {
        HistorialPagosFragment fragment = new HistorialPagosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historial_pagos, container, false);

        layout = (ViewGroup) view.findViewById(R.id.content_historial);

        //Lectura de los autos desde la BD
        //Instancia de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Referencia de todos los recibos
        DatabaseReference recibosRef = database.getReference(FirebaseReferences.RECIBOS_REFERENCE);
        recibosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se limpian las cardView infladas
                layout.removeAllViews();
                //Se meten todos los autos en un Iterable de tipo DataSnapshot
                Iterable<DataSnapshot> recibos = dataSnapshot.getChildren();
                //Se recorre ese Iterable
                while(recibos.iterator().hasNext()){
                    //Se coge uno de esos DataSnapshots (auto) que hay en el Iterable
                    DataSnapshot reciboDS = recibos.iterator().next();
                    //Se le pasa su valor a un objeto de tipo auto
                    Recibo recibo = reciboDS.getValue(Recibo.class);
                    //Se ponen los atributos de ese objeto en su correspondiente cardView
                    ponerRecibo(recibo, reciboDS.getKey());

                    /*//Se imprimen los atributos del auto
                    Log.i("Auto", auto.getNombrePersonalizado().toString());*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @SuppressLint("InlinedApi")
    private void ponerRecibo(final Recibo recibo, final String reciboKey){
        //Se definen los elementos que van en el layout para cada auto
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int id = R.layout.layout_recibo;
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);
        TextView textViewPlacaRecibo = (TextView) relativeLayout.findViewById(R.id.placa_auto_recibo);
        TextView textViewPeajeRecibo = (TextView) relativeLayout.findViewById(R.id.peaje_recibo);
        TextView textViewFechaRecibo = (TextView) relativeLayout.findViewById(R.id.fecha_recibo);
        TextView textViewCostoRecibo = (TextView) relativeLayout.findViewById(R.id.costo_recibo);
        //Se configuran los elementos
        textViewPlacaRecibo.setText(recibo.getPlaca());
        textViewPeajeRecibo.setText(recibo.getPeaje());
        textViewFechaRecibo.setText(recibo.getFecha());
        textViewCostoRecibo.setText("$"+recibo.getCosto()+" COP");

        layout.addView(relativeLayout);
        return;
    }
}
