package com.example.ejercdml;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MostrarDatosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos);

        TextView tvDatosGuardados = findViewById(R.id.tvDatosGuardados);
        TextView tvFileStreamTag = findViewById(R.id.tvFileStreamTag);

        File file = new File(getFilesDir(), MainActivity.FILE_NAME);
        String datos = MainActivity.leerDatosGuardados(file);

        tvDatosGuardados.setText(datos.isEmpty() ? "No hay datos guardados" : datos);
        tvFileStreamTag.setText("Datos recuperados de FileStream");
    }
}