package ee.ut.labimangija.arraygrader.kasutajaliides;

import ee.ut.labimangija.arraygrader.massiiviseis.MassiiviSeis;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.LäbimänguAlustamine;
import ee.ut.labimangija.arraygrader.massiivioperatsioon.Massiivioperatsioon;

import java.util.Random;
import java.util.Scanner;

// Klassi implementatsioon põhineb peamiselt Pihla Järve loodud lahendusel.
// Eeskujuks kasutatud töö: "Rakendus massiivialgoritmide läbimängude hindamiseks", kättesaadav aadressil:
// https://thesis.cs.ut.ee/2d182e41-7be8-4a84-b9fd-af9a48a8f6cc

public abstract class Kasutajaliides {

    protected abstract LäbimänguAlustamine läbimänguAlustamiseOperatsioon(int[] massiiv);

    protected abstract Massiivioperatsioon leiaOperatsioon(String[] sisend, MassiiviSeis massiiviSeis) throws ViganeSisendException;

}

