package com.grupo19.Models;

import com.grupo19.Interfaces.IFacturacao;
import com.grupo19.Interfaces.IFacturacaoPorProd;

public class FacturacaoPorProd implements IFacturacao, IFacturacaoPorProd, serializable {

    /** list de sales */
    private List<ISale> salesList;

    /** construtor por omissao */
    public FacturacaoPorProd(){
        this.salesList=getSalesList();
    }

    /** construtor de cópia */
    public FacturacaoPorProd(IFacturacaoPorProd fact){
        this.fact=getSalesList();
    }

    /** construtor parametrizado */
    public  FacturacaoPorProd(List<Sale> list){
        this.salesList=list;
    }

    /** getter da list de sales */
    public List<ISale> getSalesList(){
        ArrayList novo = new ArrayList<ISale> (this.salesList.size());
        for ( ISale s : this.salesList )
        {
            novo.add(s.clone());
        }
        return novo;
    }


    /** setter da list de sales */
    @param list de sales
    public void setSalesList(List<ISale> salesAll){
        this.salesList= new ArrayList<List> (salesAll.size());
        for ( ISale s: salesAll)
        {
            salesList.add(s.clone());
        }
    }

    public IFacturacaoPorProd clone(){
        return new IFacturacaoPorProd(f: this);
    }

    void List<ISale> addSale(ISale s){
        salesList.add(s.clone());
    }

    void list<ISale> removesale(ISale s){
        salesList.remove(s);
    }

}