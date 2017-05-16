package co.edu.udea.telepeaje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.edu.udea.telepeaje.Objetos.Auto;
import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.Pago;



public class MisAutosFragment extends Fragment {

    //Referencia a elementos de la interfaz
    TextView textViewNombreUsuario;
    TextView textViewLogOut;
    FloatingActionButton buttonAgregarAuto;
    private ViewGroup layout;

    //Listener para la base de datos
    FirebaseAuth.AuthStateListener mAuthListener;

    //Referencia al usuario actual en la base de datos
    DatabaseReference usuarioRef;

    private OnFragmentInteractionListener mListener;

    public MisAutosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mis_autos, container, false);

        textViewNombreUsuario = (TextView) view.findViewById(R.id.nombre_usuario_text_view);
        textViewLogOut = (TextView) view.findViewById(R.id.log_out_text_view);
        buttonAgregarAuto = (FloatingActionButton) view.findViewById(R.id.agregar_auto_button);
        layout = (ViewGroup) view.findViewById(R.id.content);

        //Se ponen los datos de la cuenta en los elementos de la interfaz
        textViewNombreUsuario.setText("¡Hola "+ FirebaseAuth.getInstance().getCurrentUser().getEmail()+"!");

        //Listener para la autenticación
        mAuthListener = new FirebaseAuth.AuthStateListener(){

            //Cuando cambia la sesion (inicia o cierra)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){//Sesion cerrada
                    Log.i("SESSION", "sesión cerrada");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        };

        buttonAgregarAuto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                agregarAuto(v);
            }
        });

        textViewLogOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                logout();
            }
        });

        //Lectura de los autos desde la BD
        //Instancia de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Referencia de todos los usuarios
        DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
        //Se carga la referencia al usuario actual mediante el SharedPreferences
        //El archivo PreferenciasUsuario sólo sera accedido por esta aplicación
        SharedPreferences misPreferencias = getActivity().getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        String UID = misPreferencias.getString("UID", "");
        usuarioRef = usuariosRef.child(UID);
        //Referencia a todos los autos del usuario actual
        DatabaseReference autosRef = usuarioRef.child(FirebaseReferences.AUTOS_REFERENCE);
        autosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se limpian las cardView infladas
                layout.removeAllViews();
                //Se meten todos los autos en un Iterable de tipo DataSnapshot
                Iterable<DataSnapshot> autos = dataSnapshot.getChildren();
                //Se recorre ese Iterable
                while(autos.iterator().hasNext()){
                    //Se coge uno de esos DataSnapshots (auto) que hay en el Iterable
                    DataSnapshot autoDS = autos.iterator().next();
                    //Se le pasa su valor a un objeto de tipo auto
                    Auto auto = autoDS.getValue(Auto.class);
                    //Se ponen los atributos de ese objeto en su correspondiente cardView
                    ponerAuto(auto, autoDS.getKey());

                    /*//Se imprimen los atributos del auto
                    Log.i("Auto", auto.getNombrePersonalizado().toString());*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void agregarAuto(View view){
        Intent intent = new Intent(getActivity(), InformacionVehiculoActivity.class);
        String origen = getActivity().getLocalClassName();
        intent.putExtra("claseOrigen", origen);
        intent.putExtra("usuarioKey", getActivity().getIntent().getStringExtra("usuarioKey"));
        startActivity(intent);
    }

    //En este caso la view es la cardView que invoca a este metodo
    public void openMiAuto(View view){
        Intent intent = new Intent(getActivity(), MiAutoActivity.class);
        //Segun el tag de la cardView, se extrae el texto de los elementos que esta contiene
        TextView placaTextView = (TextView) view.findViewById(R.id.placa_text_view);
        String placa = placaTextView.getText().toString();
        intent.putExtra("editTextPlaca", placa);
        TextView nombreAutoTextView = (TextView) view.findViewById(R.id.nombre_auto_text_view);
        String nombreAuto = nombreAutoTextView.getText().toString();
        intent.putExtra("nombreAuto", nombreAuto);
        TextView pagoTextView = (TextView) view.findViewById(R.id.pago_text_view);
        String pago = pagoTextView.getText().toString();
        intent.putExtra("pago", pago);
        TextView peajesTextView = (TextView) view.findViewById(R.id.peajes_habilitados_text_view);
        String peajes = peajesTextView.getText().toString();
        intent.putExtra("peajes", peajes);
        CardView autoCardView = (CardView) view.findViewById(R.id.auto_card_view);
        String tagCardView = autoCardView.getTag().toString();
        intent.putExtra("tagCardView", tagCardView);

        //Se pone vacía la actividad desde la cual se configuró el auto, ya que en esta actividad no lo estamos configurando
        ConfiguracionAuto.setActivity("");
        startActivity(intent);
    }

    //Cierra la sesión
    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Log.i("SESSION", "sesión cerrada");
    }

    @SuppressLint("InlinedApi")
    private void ponerAuto(final Auto auto, final String autoKey){
        //Se coge el pago que está asociado al auto
        //Referencia a todos los pagos del usuario actual
        DatabaseReference pagosRef = usuarioRef.child(FirebaseReferences.PAGOS_REFERENCE);
        pagosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se meten todos los pagos en un Iterable de tipo DataSnapshot
                Iterable<DataSnapshot> pagos = dataSnapshot.getChildren();
                //Se define el pago que se intenta buscar
                Pago pago = null;
                //Se recorre ese Iterable para buscar el pago asociado al auto
                while(pagos.iterator().hasNext()){
                    //Se coge uno de esos DataSnapshots (auto) que hay en el Iterable
                    DataSnapshot pagoDS = pagos.iterator().next();
                    //Se le pasa su valor a un objeto de tipo auto
                    if((pagoDS.getKey()==auto.getIdPagoCorrespondiente())||(pagoDS.getKey().equals(auto.getIdPagoCorrespondiente()))){
                        pago = pagoDS.getValue(Pago.class);
                        //Se definen los elementos que van en el layout para cada auto
                        LayoutInflater inflater = null;
                        if(getContext()!=null){
                            inflater = LayoutInflater.from(getContext());
                            int id = R.layout.layout_card_view_auto;
                            RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);
                            CardView cardView = (CardView) relativeLayout.findViewById(R.id.auto_card_view);
                            TextView textViewNombreAuto = (TextView) relativeLayout.findViewById(R.id.nombre_auto_text_view);
                            TextView textViewPeajesHabilitados = (TextView) relativeLayout.findViewById(R.id.peajes_habilitados_text_view);
                            TextView textViewPlaca = (TextView) relativeLayout.findViewById(R.id.placa_text_view);
                            TextView textViewPago = (TextView) relativeLayout.findViewById(R.id.pago_text_view);
                            //Se configuran los elementos
                            textViewNombreAuto.setText(auto.getNombrePersonalizado().toString());
                            if(auto.getPeajesHabilitados()){
                                textViewPeajesHabilitados.setText("Todos los peajes estan habilitados");
                            }else{
                                textViewPeajesHabilitados.setText("Todos los peajes estan deshabilitados");
                            }
                            textViewPlaca.setText(auto.getPlaca().toString());
                            //Se formatea el método de pago para que no se muestre completamente
                            String numeroTarjeta = String.valueOf(pago.getNumeroTarjeta());
                            char [] numeroTarjetaFormateada = new char[9];
                            numeroTarjeta.getChars(12, 16, numeroTarjetaFormateada, 0);
                            textViewPago.setText("xxxx "+String.valueOf(numeroTarjetaFormateada));
                            //El tag de la cardView será el id del auto
                            cardView.setTag(autoKey);
                            cardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openMiAuto(v);
                                }
                            });
                            layout.addView(relativeLayout);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        //Se inicia el listener
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        //Se elimina el listener
        if(mAuthListener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
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
}
