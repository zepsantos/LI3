package com.grupo19.Models;

import com.grupo19.Interfaces.*;
import com.grupo19.Tuple;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class Facturacao implements IFacturacao, Serializable {

    /**
     *
     * string é codProd
     * IFacturacaoPorProd é uma list de sales de um produto num mes
     *
     * */
    private List<Map<String, IFacturacaoPorProd>> arrayOfSales;


    /**
     * Construtor por omissão
     */
    public Facturacao(){
        this.arrayOfSales= new ArrayList<>();
        for(int i = 0; i<12 ; i++){
            Map <String, IFacturacaoPorProd> tmp = new HashMap<>();
            arrayOfSales.add(tmp);
        }

    }

    /**
     * Construtor de cópia
     * @param  umaFacturacao
     */
    public Facturacao(IFacturacao umaFacturacao){

        this.arrayOfSales=umaFacturacao.getArrayOfSales();
    }



    /**
     * Getter da lista de Sales
     * @return nova list de sales
     */
    public List<Map<String,IFacturacaoPorProd>> getArrayOfSales(){
        List<Map<String, IFacturacaoPorProd>> nova = new ArrayList<>();
        for(int i=0;i<12;i++){
            Map <String, IFacturacaoPorProd> tmp;
            tmp=arrayOfSales.get(i);
            nova.add(i,tmp);
        }
        return nova;
    }




    /**
     * Método Equals
     * @param obj
     * @return Valor de verdade
     */
    public boolean equals(Object obj){
        if(obj == this) {
            return true;
        }
        if(obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        IFacturacao fact = (IFacturacao) obj;
        for(int i=0; i<12; i++) {
            Map <String, IFacturacaoPorProd> tmp = arrayOfSales.get(i);
            for (Map.Entry<String, IFacturacaoPorProd> entry : tmp.entrySet()){
                for(Map.Entry<String, IFacturacaoPorProd> entry2 : fact.getArrayOfSales().get(i).entrySet()){
                    String key = entry.getKey();
                    String key2 = entry2.getKey();
                    if(!key.equals(key2)) return false;
                        else{
                            if(!entry.getValue().equals(entry2.getValue())) return false;
                    }
                }
            }
        }
        return true;
    }



    /**
     *
     * adiciona uma ISale à facturacao
     * @param sale sale a adicionar
     *
     * */
    public void add (ISale sale) {
        int month=sale.getMonth() -1;
        String codprod =sale.getProduct();
        IFacturacaoPorProd value = arrayOfSales.get(month).get(codprod);
        if(value == null) {
            value = new FacturacaoPorProd();
            Map<String,IFacturacaoPorProd> mapTmp = arrayOfSales.get(month);
            mapTmp.put(codprod,value);
        }
        value.addSale(sale.clone());

    }
    /**
     * metodo de clone
     *
     */
    public IFacturacao clone() {
        return new Facturacao(this);
    }


    /**
     *
     * Faturacao mensal total
     * @param month de consulta
     * @return total faturado
     *
     * */

    public double valorTotalFactMensal (int month) {
        double total=0;
        Map <String, IFacturacaoPorProd> tmp;
        tmp=arrayOfSales.get(month-1);
        for(Map.Entry<String,IFacturacaoPorProd> entry : tmp.entrySet()) {
            IFacturacaoPorProd arrayMonth = entry.getValue();
            total += arrayMonth.totalSaleProd();
        }
        return total;
    }

    /**
     *
     * Faturacao dos doze meses
     * @return total faturado
     *
     */
    public double facturacaoTotal() {
        double total = 0.0;
        for(int i = 1; i<=12 ; i++) {
            total += this.valorTotalFactMensal(i);
        }
        return total;
    }



    /**
     *
     * Calcula a list por mes, de clientes que compraram o produto passado como parametro
     * @param codProd string id do produto
     * @return resultados list de clientes
     *
     */
    public List<Integer> numberOfClientsWhoBoughtPerMonth(String codProd) {
        List<Integer> resultados = new ArrayList<>();

        for(int i=0;i<12;i++){
            int total;
            HashSet<String> clients = new HashSet<>();
            Map <String, IFacturacaoPorProd> tmp;
            tmp=arrayOfSales.get(i);
            IFacturacaoPorProd tmpFact = tmp.get(codProd);
            if(tmpFact == null ){
                resultados.add(i,0);
                continue;
            }
            for(ISale s: tmpFact.getSalesList()){
                clients.add(s.getClient());
            }
            total=clients.size();
            resultados.add(i,total);
        }
        return resultados;
    }

    /**
     *
     * Calcula o número total de clientes dos dozes meses que compraram o produto
     * @param codProd string id de produto
     * @return devolve valor total
     *
     */
    public int numberOfClientsWhoBought(String codProd) {
        int total = 0;
        List<Integer> numberOfClientsPerMonth = numberOfClientsWhoBoughtPerMonth(codProd);
        for(Integer i : numberOfClientsPerMonth) {
            total += i;
        }
        return total;
    }

    /**
     *
     * valor total mensal, de um dado produto(price*units)
     * @param codProd
     * @return res
     *
     */
    public List<Double> totalSalesPerProduct ( String codProd){
        List<Double> res= new ArrayList<>();
        for(int i=0;i<12;i++){
            double totalMonth=0;
            Map <String, IFacturacaoPorProd> tmp;
            tmp=arrayOfSales.get(i);
            for(Map.Entry<String,IFacturacaoPorProd> entry : tmp.entrySet()) {
                String key = entry.getKey();
                if(key.equals(codProd)){
                    totalMonth += entry.getValue().totalSaleProd();
                }
            }
            res.add(i,totalMonth);
        }
        return  res;
    }

    /**
     *
     * calcula quantas vezes o produto foi comprado, por quantos clientes diferentes e total facturado por mês
     * @param product string do id do produto
     * @return res list
     *
     */
    public List<List<Double>> getNumClientAndFacturacao(String product){
        List<List<Double>> res = new ArrayList<>();
        for(int i=0; i < 12; i++) res.add(new ArrayList<>());
        double qnt,nclientes,facturado;
        for(int i = 0; i <12 ;i++){
            IFacturacaoPorProd factProd = this.arrayOfSales.get(i).get(product);
            if(factProd == null) continue;
            qnt = (double) factProd.getSalesList().size();
            nclientes = factProd.getDifClientsWhoBought();
            facturado = factProd.totalSaleProd();
            res.get(i).add(qnt);
            res.get(i).add(nclientes);
            res.get(i).add(facturado);

        }
        return res;

    }

    /**
     *
     * Método que determina a facturacao por filial e por mês do produto prod
     * @param prod
     * @return res
     */
    public List<List<Double>> facturacaoPerProdPerFilialPerMonth(String prod) {
        List<List<Double>> res = new ArrayList<>();
        for(int i = 0; i<12; i++) {
            Map<String,IFacturacaoPorProd> tmp = this.arrayOfSales.get(i);
            IFacturacaoPorProd facProd = tmp.get(prod);
            if(facProd == null) {
                res.add(new ArrayList<>());
                continue;
            }
            res.add(facProd.factPerFilial());
        }
        return res;

    }

    /**
     *
     * Determina os clientes que mais compraram um produtoe quanto gastaram
     *      * @param produto string id do produto
     * @param tamanho número de clientes que mais compraram o produto
     * @return res lista segundo os critérios de ordenação
     *
     */
    public List<Map.Entry<String, ITuple<Integer,Double>>> getXClientsWhoMostBoughtProduct(String produto, int tamanho){
        Map<String, ITuple<Integer,Double>>  mapa = new HashMap<>();
        List<Map.Entry<String, ITuple<Integer,Double>>> res;
        int count;
        double facturacao;
        for(int i = 0; i< 12; i++){
            IFacturacaoPorProd factProd = this.arrayOfSales.get(i).get(produto);
            if(factProd == null)continue;
            for(ISale sale : factProd.getSalesList()){
                if(mapa.containsKey(sale.getClient())){
                    count = mapa.get(sale.getClient()).getFirstElem();
                    facturacao = mapa.get(sale.getClient()).getSecondElem();
                    count += sale.getUnits();
                    facturacao += sale.totalPrice();
                    mapa.put(sale.getClient(),new Tuple<>(count,facturacao));
                }else {
                    mapa.put(sale.getClient(),new Tuple<>(sale.getUnits(),sale.totalPrice()));
                }
            }
        }
        res = mapa.entrySet().stream().sorted((o1,o2)-> o2.getValue().getFirstElem().compareTo(o1.getValue().getFirstElem())).collect(Collectors.toList());
        Collections.reverse(res); // com isto fica do maior para o menor
        res = res.stream().limit(tamanho).collect(Collectors.toList());
        Collections.reverse(res);
        return res;

    }


    // implementar uma arvore com um tuple de string que é o cliente
    // mais outro tuple que é a quantidade e o montante
    // fazer tb o comparador adequado
    /*
    public Set<ITuple<String, ITuple<Integer,Double>>> experimento(String produto,int tamanho){

        Set<ITuple<String, ITuple<Integer,Double>>> arvore = new TreeSet<ITuple<String, ITuple<Integer,Double>>>();
        for(int i = 0; i<12;i++){
            IFacturacaoPorProd factProd = this.arrayOfSales.get(i).get(produto);
            if(factProd == null)continue;
            for(ISale sale : factProd.getSalesList()){
                if(arvore.contains(sale.getClient())){

                }else{

                }
            }


        }
    }*/


}
