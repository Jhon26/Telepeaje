package co.edu.udea.telepeaje;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import co.edu.udea.telepeaje.Objetos.Auto;
import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.Pago;
import co.edu.udea.telepeaje.Objetos.Usuario;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoPersonalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoPersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoPersonalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Referencias a elementos de la interfaz
    EditText editTextNombres;
    EditText editTextApellidos;
    Spinner spinnerCodigoArea;
    EditText editTextCelular;
    FloatingActionButton buttonSiguiente;

    //Datos para el registro del usuario
    String nombres;
    String apellidos;
    String celular;
    Usuario usuario;

    Usuario usuario2;

    private OnFragmentInteractionListener mListener;

    public InfoPersonalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoPersonalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoPersonalFragment newInstance(String param1, String param2) {
        InfoPersonalFragment fragment = new InfoPersonalFragment();
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
        View view = inflater.inflate(R.layout.fragment_info_personal, container, false);

        //Referencias a elementos de la interfaz
        editTextNombres = (EditText) view.findViewById(R.id.numero_doc_propietario_edit_text);
        editTextApellidos = (EditText) view.findViewById(R.id.nombre_personalizado_edit_text);
        spinnerCodigoArea = (Spinner) view.findViewById(R.id.tipo_pago_spinner);
        editTextCelular = (EditText) view.findViewById(R.id.placa_edit_text);

        //Configuración del spinner
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource( getContext(), R.array.codigos_area, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCodigoArea.setAdapter(spinnerAdapter);

        buttonSiguiente = (FloatingActionButton) view.findViewById(R.id.info_personal_button);



        if(getArguments().getBoolean("origen")){
            FirebaseDatabase database  = FirebaseDatabase.getInstance();
            DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
            SharedPreferences misPreferencias = getActivity().getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
            String UID = misPreferencias.getString("UID", "");
            DatabaseReference usuarioRef = usuariosRef.child(UID);
            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    usuario2 = dataSnapshot.getValue(Usuario.class);
                    editTextNombres.setText(usuario2.getNombres());
                    editTextApellidos.setText(usuario2.getApellidos());
                    String celular = String.valueOf(usuario2.getCelular());
                    char[] celularFormateado = new char[10];
                    celular.getChars(2, celular.length(), celularFormateado, 0);
                    editTextCelular.setText(String.valueOf(celularFormateado));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            buttonSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //actualizarUsuario(v);
                }
            });
        }else{
            buttonSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openInformacionVehiculo(v);
                }
            });
        }

        return view;
    }

    public void openInformacionVehiculo(View view){
        //Validación de datos
        nombres = editTextNombres.getText().toString().trim();
        apellidos = editTextApellidos.getText().toString().trim();
        celular = spinnerCodigoArea.getSelectedItem().toString().concat(editTextCelular.getText().toString().trim());
        if((nombres==null)||(nombres.equals(""))){
            editTextNombres.setError("Ingrese el nombre");
            Log.e("SESSION", "Ingrese el nombre");
        }else if((apellidos==null)||(apellidos.equals(""))){
            editTextApellidos.setError("Ingrese el apellido");
            Log.e("SESSION", "Ingrese el apellido");
        }else if((celular==null)||(celular.equals(""))){
            editTextCelular.setError("Ingrese el número de celular");
            Log.e("SESSION", "Ingrese el número de celular");
        }else{
            //Se continua con la construcción del usuario
            usuario = (Usuario) getArguments().getSerializable("usuario");
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setCelular(Long.parseLong(celular));

            //Finalmente se escribe el usuario en la base de datos.
            // Primero se toma la instancia de la BD
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Se coge la referencia al nodo de todos los usuarios
            final DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
            //Se carga la referencia al usuario actual mediante el SharedPreferences
            //El archivo PreferenciasUsuario sólo sera accedido por esta aplicación
            SharedPreferences misPreferencias = getActivity().getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
            String UID = misPreferencias.getString("UID", "");
            //Se le añade un hijo que tenga como key el UID asignado para este usuario
            final DatabaseReference usuarioRef = usuariosRef.child(UID);
            //Se le pone valor a esa referencia (a ese usuario)
            usuarioRef.setValue(usuario);

            //Construcción del intent
            Intent intent = new Intent(getActivity(), InformacionPagoActivity.class);
            String origen = getActivity().getLocalClassName();
            intent.putExtra("claseOrigen", origen);

            //Se inicia la actividad
            startActivity(intent);
        }
    }

    public void actualizarUsuario(View view){
        //Validación de datos
        nombres = editTextNombres.getText().toString().trim();
        apellidos = editTextApellidos.getText().toString().trim();
        celular = spinnerCodigoArea.getSelectedItem().toString().concat(editTextCelular.getText().toString().trim());
        if((nombres==null)||(nombres.equals(""))){
            editTextNombres.setError("Ingrese el nombre");
            Log.e("SESSION", "Ingrese el nombre");
        }else if((apellidos==null)||(apellidos.equals(""))){
            editTextApellidos.setError("Ingrese el apellido");
            Log.e("SESSION", "Ingrese el apellido");
        }else if((celular==null)||(celular.equals(""))){
            editTextCelular.setError("Ingrese el número de celular");
            Log.e("SESSION", "Ingrese el número de celular");
        }else{
            FirebaseDatabase database  = FirebaseDatabase.getInstance();
            DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
            SharedPreferences misPreferencias = getActivity().getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
            String UID = misPreferencias.getString("UID", "");
            DatabaseReference usuarioRef = usuariosRef.child(UID);
            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    usuario2 = dataSnapshot.getValue(Usuario.class);
                    usuario2.setNombres(nombres);
                    usuario2.setApellidos(apellidos);
                    usuario2.setCelular(Long.parseLong(celular));

                    //Finalmente se actualiza el usuario en la base de datos.
                    // Primero se toma la instancia de la BD
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //Se coge la referencia al nodo de todos los usuarios
                    final DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
                    //Se carga la referencia al usuario actual mediante el SharedPreferences
                    //El archivo PreferenciasUsuario sólo sera accedido por esta aplicación
                    SharedPreferences misPreferencias = getActivity().getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
                    String UID = misPreferencias.getString("UID", "");
                    //Se le añade un hijo que tenga como key el UID asignado para este usuario
                    final DatabaseReference usuarioRef = usuariosRef.child(UID);
                    //Se le pone valor a esa referencia (a ese usuario)
                    usuarioRef.setValue(usuario2);


                    //Se modifican los datos de pago del usuario
                    Map<String, Object> usuarioMap = usuario2.toMap();
                    Map<String, Object> usuarioActualizacion = new HashMap<>();
                    String ruta = "/"+UID;
                    usuarioActualizacion.put(ruta, usuarioMap);
                    usuariosRef.updateChildren(usuarioActualizacion);

                    //Se le pone al usuario los hijos que tenia
                    Iterable<DataSnapshot> hijosDS = dataSnapshot.getChildren();
                    while(hijosDS.iterator().hasNext()){
                        DataSnapshot hijoDS = hijosDS.iterator().next();
                        if(hijoDS.getKey().equals(FirebaseReferences.AUTOS_REFERENCE)){
                            Iterable<DataSnapshot> autosDS = hijoDS.getChildren();
                            while(autosDS.iterator().hasNext()){
                                DataSnapshot autoDS = autosDS.iterator().next();
                                Auto auto = autoDS.getValue(Auto.class);
                                usuarioRef.child(FirebaseReferences.AUTOS_REFERENCE).push().setValue(auto);
                            }
                        }else{
                            Iterable<DataSnapshot> pagosDS = hijoDS.getChildren();
                            while(pagosDS.iterator().hasNext()){
                                DataSnapshot pagoDS = pagosDS.iterator().next();
                                Pago pago = pagoDS.getValue(Pago.class);
                                usuarioRef.child(FirebaseReferences.PAGOS_REFERENCE).push().setValue(pago);
                            }
                        }
                    }


                    //Se informa que el usuario ha sido actualizado
                    Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_LONG);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    /*
    //Al cerrar está actividad, el registro queda incompleto, por lo tanto...
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Se borran los datos del usuario creado
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        String UID = misPreferencias.getString("UID", "");//Con usuario.getUID() tampoco funciona
        final DatabaseReference borrar = FirebaseDatabase.getInstance().getReference(FirebaseReferences.USUARIOS_REFERENCE).child(UID);
        Log.i("SESSION", "El UID del usuario a eliminar es "+borrar.toString());
        borrar.removeValue();
        //Se borra el usuario creado
        FirebaseAuth sesion = FirebaseAuth.getInstance();
        FirebaseUser usuario = sesion.getCurrentUser();
        usuario.delete();
    }
    */

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
