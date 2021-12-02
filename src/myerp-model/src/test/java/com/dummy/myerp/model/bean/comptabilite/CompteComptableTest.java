package com.dummy.myerp.model.bean.comptabilite;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CompteComptableTest {

    @Test
    public void getByNumero() {

    	List<CompteComptable> vListComte = new ArrayList<>();
    	
    	CompteComptable vComte;
    	vComte = new CompteComptable();
    	
    	vComte.setLibelle("Libelle1");
    	vComte.setNumero(1);

    	CompteComptable vComte2;
    	vComte2 = new CompteComptable();
    	
    	vComte2.setLibelle("Libelle2");
    	vComte2.setNumero(2);
    	
    	vListComte.add(vComte);
    	vListComte.add(vComte2);
    	
    	Assert.assertTrue(vComte.getByNumero(vListComte, 1).getLibelle().equals("Libelle1"));
    	Assert.assertTrue(vComte.getByNumero(vListComte, 2).getLibelle().equals("Libelle2"));
    	Assert.assertNull(vComte.getByNumero(vListComte, 3));
    }

}
