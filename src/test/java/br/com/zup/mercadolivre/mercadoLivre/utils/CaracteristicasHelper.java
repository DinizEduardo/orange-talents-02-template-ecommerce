package br.com.zup.mercadolivre.mercadoLivre.utils;

import br.com.zup.mercadolivre.mercadoLivre.model.Caracteristica;
import br.com.zup.mercadolivre.mercadoLivre.model.request.CaracteristicaRequest;

import java.util.ArrayList;
import java.util.List;

public class CaracteristicasHelper {

    public static List<Caracteristica> geraCaracteristicas(int n) {
        List<Caracteristica> caracteristicas = new ArrayList<Caracteristica>();
        for (int i = 0; i < n; i++)
            caracteristicas.add(new Caracteristica("Nome" + i, "Valor"));

        return caracteristicas;
    }

    public static List<CaracteristicaRequest> geraCaracteristicasRequest(int n) {
        List<CaracteristicaRequest> caracteristicas = new ArrayList<CaracteristicaRequest>();
        for (int i = 0; i < n; i++)
            caracteristicas.add(new CaracteristicaRequest("Nome" + i, "Valor"));

        return caracteristicas;
    }

}
