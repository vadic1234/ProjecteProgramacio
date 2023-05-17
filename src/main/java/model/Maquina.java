package model;

import daos.SlotDAO_MySQL;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Maquina { //Classe màquina per guardar la informació que es vol mostrar
    private String slot_posicio;
    private String nom_producte;
    private String quantitat_stock;
    private Float beneficis;

}