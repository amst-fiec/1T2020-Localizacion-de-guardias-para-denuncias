package TwoReport.com.project.Database;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;

import TwoReport.com.project.Administrador;
import TwoReport.com.project.AdministradorPackage.AdminMainActivity;
import TwoReport.com.project.CrimeInfo;
import TwoReport.com.project.DataBase;
import TwoReport.com.project.Guardia;
import TwoReport.com.project.GuardiaPackage.GuardMainActivity;
import TwoReport.com.project.Location;
import TwoReport.com.project.Login;
import TwoReport.com.project.R;
import TwoReport.com.project.RegisterActivity;
import TwoReport.com.project.Usuario;
import TwoReport.com.project.UsuarioPackage.MainActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DataBaseHandler {
    FirebaseDatabase db;

    public DataBaseHandler(FirebaseDatabase db) {
        this.db = db;
    }

    public void iniciarSesion(Context contexto,String uid,HashMap<String,String> info_user){
        DatabaseReference UserRef = this.db.getReference("Usuarios/"+uid);
        DatabaseReference GuardRef = this.db.getReference("Guardias/"+uid);
        DatabaseReference AdminRef = this.db.getReference("Administradores/"+uid);

        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Intent intent = new Intent(contexto, MainActivity.class);
                    intent.putExtra("info_user", info_user);
                    contexto.startActivity(intent);
                }else{
                    GuardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            if (snapshot1.exists()){
                                Intent intent = new Intent(contexto, GuardMainActivity.class);
                                intent.putExtra("info_user", info_user);
                                contexto.startActivity(intent);
                            }else{
                                AdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                        if(snapshot2.exists()){
                                            Intent intent = new Intent(contexto, AdminMainActivity.class);
                                            intent.putExtra("info_user", info_user);
                                            contexto.startActivity(intent);
                                        }else{
                                            Toast.makeText(contexto,"Usuario no registrado",Toast.LENGTH_LONG);
                                            FirebaseAuth.getInstance().signOut();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void registrarUsuario(String uid, Usuario usuario, Context contexto,HashMap<String, String> info_user){
        DatabaseReference userRef = this.db.getReference("Usuarios/"+uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(contexto,"Este correo ya ha sido usado",Toast.LENGTH_LONG).show();
                }else{
                    userRef.setValue(usuario)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    Toast.makeText(contexto,"Usuario registrado correctamente",Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(contexto,MainActivity.class);
                                    i.putExtra("info_user",info_user);
                                    contexto.startActivity(i);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    Toast.makeText(contexto,"Fallo en registro de Usuario",Toast.LENGTH_LONG).show();

                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void registrarGuardia(String uid, Guardia guardia, Context contexto,String token,HashMap<String, String> info_user){
        DatabaseReference userRef = this.db.getReference("Guardias/"+uid);
        DatabaseReference tokenRef = this.db.getReference("Tokens/"+token);
        tokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot0) {
                if (snapshot0.exists()){
                    if (snapshot0.getValue().toString().equals("Guardias")){
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Toast.makeText(contexto,"Este correo ya ha sido usado",Toast.LENGTH_LONG).show();
                                }else{
                                    userRef.setValue(guardia)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Write was successful!
                                                    Toast.makeText(contexto,"Guardia registrado correctamente",Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(contexto,GuardMainActivity.class);
                                                    i.putExtra("info_user",info_user);
                                                    contexto.startActivity(i);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Write failed
                                                    Toast.makeText(contexto,"Fallo en registro de Guardia",Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void registrarAdministrador(String uid, Administrador administrador, Context contexto,String token,HashMap<String, String> info_user){
        DatabaseReference userRef = this.db.getReference("Administradores/"+uid);
        DatabaseReference tokenRef = this.db.getReference("Tokens/"+token);
        tokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot0) {
                if (snapshot0.exists()){
                    if (snapshot0.getValue().toString().equals("Administradores")){
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Toast.makeText(contexto,"Este correo ya ha sido usado",Toast.LENGTH_LONG).show();
                                }else{
                                    userRef.setValue(administrador)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Write was successful!
                                                    Toast.makeText(contexto,"Administrador registrado correctamente",Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(contexto,AdminMainActivity.class);
                                                    i.putExtra("info_user",info_user);
                                                    contexto.startActivity(i);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Write failed
                                                    Toast.makeText(contexto,"Fallo en registro de Administrador",Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }else{
                        Toast.makeText(contexto,"Token invalido",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(contexto,"Token invalido",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getGeoLocalizacion(String uid, GoogleMap mMap, Resources resources,Location location){
        DatabaseReference ubicacionRef = this.db.getReference("Ubicacion/"+uid);
        DatabaseReference guardiasRef = this.db.getReference("Guardias/");

        System.out.println("JUJU");

        ubicacionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("Entro aqui");
                System.out.println(snapshot);
                if (snapshot.exists()){
                    mMap.clear();
                    Double latitud = (Double) snapshot.child("latitud").getValue();
                    Double longitud = (Double) snapshot.child("longitud").getValue();
                    location.setLatitud(latitud);
                    location.setLongitud(longitud);
                    LatLng posicion = new LatLng(latitud, longitud);
                    mMap.addMarker(new MarkerOptions().position(posicion).title("Estas Aquí"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
                    mMap.setMinZoomPreference(18);
                    ArrayList<Marker> markers = new ArrayList();
                    guardiasRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (Marker m:markers){
                                m.remove();
                            }

                            for (DataSnapshot guardSnapShot:snapshot.getChildren()){
                                System.out.println(guardSnapShot);
                                Double latitud = (Double) guardSnapShot.child("latitud").getValue();
                                Double longitud = (Double) guardSnapShot.child("longitud").getValue();
                                LatLng latLngGuard = new LatLng(latitud,longitud);
                                Marker marker = mMap.addMarker(new MarkerOptions().position(latLngGuard).icon(getBitmapDescriptor(R.drawable.ic_guardia,resources)));
                                markers.add(marker);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        DataBase dataBase = new DataBase();
//        LatLng espol = new LatLng(-2.147207, -79.965874);
//        mMap.addMarker(new MarkerOptions().position(espol).title("Estas Aquí"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(espol));
//        mMap.setMinZoomPreference(18);
//
//
//        for (LatLng latlng : dataBase.getGuardias()) {
//            mMap.addMarker(new MarkerOptions().position(latlng).icon(getBitmapDescriptor(R.drawable.ic_guardia,resources)));
//        }
    }

    public void enviarReporte(HashMap<String, String> info_user, Location ubicacion, String descripcion,String lugar, Date date, String tipoDeCrimen,Context contexto){
        Usuario user = new Usuario(info_user.get("user_name"),info_user.get("user_email"),info_user.get("user_phone"),info_user.get("user_photo"));
        final String DATE_FORMAT = "dd-MM-yyyy kk:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
//        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String fecha = dateFormat.format(date);
        CrimeInfo reporte = new CrimeInfo(user,ubicacion,descripcion,lugar,fecha,tipoDeCrimen);
        this.db.getReference().child("Denuncia").push().setValue(reporte)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(contexto,"REPORTADO", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(contexto, MainActivity.class);
                intent.putExtra("info_user",info_user);
                contexto.startActivity(intent);
            }
        });
    }

    private BitmapDescriptor getBitmapDescriptor(int id, Resources resources) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable vectorDrawable = (VectorDrawable) resources.getDrawable(id);
            int h = vectorDrawable.getIntrinsicHeight();
            int w = vectorDrawable.getIntrinsicWidth();
            vectorDrawable.setBounds(0, 0, w, h);
            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bm);
        } else {
            return BitmapDescriptorFactory.fromResource(id);
        }
    }

}
