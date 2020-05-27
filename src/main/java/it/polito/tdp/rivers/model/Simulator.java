package it.polito.tdp.rivers.model;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class Simulator {
	
	private double Q=-1.0;
	private double C=-1.0;
	private double probabilita=0.05;//Indica la probabilità di avere un evento di richiesta straordinaria
	private int entita=10;//Indica quante volte l'evento straordinario è piu'grande dell'ordinario
	private double proporzione=0.8; //Indica la proporzione del flusso minimo rapportato al flusso medio
	
	private LinkedList<Double> occupazioneGiornaliera;
	private PriorityQueue<Event> coda;
	
	private int insoddisfatti;
	//Lista delle capienze giorno per giorno
	
	
	public void init(double k, River fiume) {
	coda=new PriorityQueue<Event>();
	occupazioneGiornaliera= new LinkedList<Double>();
	
	//Calcolo la capacità Q del bacino a disposizione
	Q= k*fiume.getFlowAvg()*30*60*60*24;
	
	//Calcolo C iniziale
	C=Q/2;
	occupazioneGiornaliera.add(C);
	
	//Setto a 0 il numero di insoddisfatti
	insoddisfatti=0;
	
	//Riempio la coda
	
	for (Flow f: fiume.getFlows()) {
		double flowIn= f.getFlow()*60*60*24;
		double flowOut=generaFlussoUscita(fiume.getFlowAvg());
		Event e = new Event(f.getDay(), flowIn, flowOut);
		coda.add(e);
	}
	
	//Faccio la simulazione vera e propria anzichè usare un metodo run
	
	while(!coda.isEmpty())
	{
		Double quantitaOdierna= occupazioneGiornaliera.get(occupazioneGiornaliera.size()-1);
		Event oggi =coda.poll();
		
		//Gestisco flusso in
			quantitaOdierna+=oggi.getFlowIn();
			if (quantitaOdierna>Q)
			{
				System.out.println("TRACIMAZIONE");
				quantitaOdierna=Q;
			}
			
		//Gestisco flusso out
		quantitaOdierna=quantitaOdierna-oggi.getFlowOut();
		if (quantitaOdierna<0)
		{
			System.out.println("CARENZA D'ACQUA");
			quantitaOdierna=0.0;
			insoddisfatti++;
		}
		occupazioneGiornaliera.add(quantitaOdierna);
	}
	
	
	}

	private double generaFlussoUscita(double flowAvg) {
		double p= Math.random();
		if (p>probabilita)//Caso di portata ordinaria
			return flowAvg*proporzione*60*60*24;
		else //Evento straordinario
			return flowAvg*proporzione*entita*60*60*24;
	}
	
	public double getOccupazioneMedia() {
		double somma=0.0;
		for (Double d: occupazioneGiornaliera)
		{
			somma+=d;
		}
		return somma/occupazioneGiornaliera.size();
	}

	public int getInsoddisfatti() {
		return insoddisfatti;
	}
	
	public int getGiorniTotali() {
		return occupazioneGiornaliera.size();
	}
	
	public double getQ() {
		return Q;
	}
}
