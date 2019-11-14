package com.sge.mobile.infrastructure.data.serviceagents;


import com.sge.mobile.application.services.CategoryAppService;
import com.sge.mobile.application.services.ProductAppService;
import com.sge.mobile.domain.model.Accesorio;
import com.sge.mobile.domain.model.Mesa;
import com.sge.mobile.domain.model.Producto;
import com.sge.mobile.domain.model.ResumenMesa;
import com.sge.mobile.domain.model.ResumenMesaDetalle;
import com.sge.mobile.domain.model.Rubro;
import com.sge.mobile.domain.model.SGEOrderServiceAgent;
import com.sge.mobile.domain.model.Sector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 04/04/14.
 */
public class SGEOrderServiceAgentImpl implements SGEOrderServiceAgent {
    @Override
    public int logIn(String user, String password, String serviceUrl) {
        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "LogIn";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/LogIn";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("user", user);
            request.addProperty("password", password);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            return Integer.parseInt(result.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<Rubro> synchcronizeCategories(String serviceUrl) {
        List<Rubro> categories = new ArrayList<Rubro>();

        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "SynchcronizeCategories";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/SynchcronizeCategories";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);

            SoapObject resSoap = (SoapObject) envelope.getResponse();

            for (int i = 0; i < resSoap.getPropertyCount(); i++) {
                SoapObject ic = (SoapObject) resSoap.getProperty(i);

                Rubro category = new Rubro();
                category.setDescripcion(ic.getProperty("Descripcion").toString());
                category.setEstado(Integer.parseInt(ic.getProperty("Estado").toString()));
                category.setId(Integer.parseInt(ic.getProperty("RubroId").toString()));

                categories.add(category);
            }
            return categories;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Producto> synchronizeProducts(CategoryAppService categoryAppService, String serviceUrl) {
        List<Producto> products = new ArrayList<Producto>();

        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "SynchronizeProducts";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/SynchronizeProducts";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);

            SoapObject resSoap = (SoapObject) envelope.getResponse();

            for (int i = 0; i < resSoap.getPropertyCount(); i++) {
                SoapObject ic = (SoapObject) resSoap.getProperty(i);

                Rubro category = categoryAppService.findCategoryById(Integer.parseInt(ic.getProperty("RubroId").toString()));
                if (category != null) {
                    Producto product = new Producto();
                    product.setDescripcion(ic.getProperty("Descripcion").toString());
                    product.setAccesorio(Boolean.parseBoolean(ic.getProperty("EsAccesorio").toString()));
                    product.setVisible(Boolean.parseBoolean(ic.getProperty("EsVisible").toString()));
                    product.setEstado(Integer.parseInt(ic.getProperty("Estado").toString()));
                    product.setPrecio(Float.parseFloat(ic.getProperty("Precio").toString()));
                    product.setId(Integer.parseInt(ic.getProperty("ProductoId").toString()));
                    product.setRubro(category);

                    products.add(product);
                }
            }
            return products;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Accesorio> synchronizeAccessories(ProductAppService productAppService, String serviceUrl) {
        List<Accesorio> accessories = new ArrayList<Accesorio>();

        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "SynchronizeAccessories";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/SynchronizeAccessories";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);

            SoapObject resSoap = (SoapObject) envelope.getResponse();

            for (int i = 0; i < resSoap.getPropertyCount(); i++) {
                SoapObject ic = (SoapObject) resSoap.getProperty(i);

                Producto prod = productAppService.findProductById(Integer.parseInt(ic.getProperty("ProductoId").toString()));
                Producto parentProduct = productAppService.findProductById(Integer.parseInt(ic.getProperty("ProductoPadreId").toString()));
                if (prod != null && parentProduct != null) {
                    Accesorio accessorie = new Accesorio();
                    accessorie.setId(Integer.parseInt(ic.getProperty("AccesorioId").toString()));
                    accessorie.setPorDefecto(Boolean.parseBoolean(ic.getProperty("EsPorDefecto").toString()));
                    accessorie.setProducto(prod);
                    accessorie.setProductoPadre(parentProduct);

                    accessories.add(accessorie);
                }
            }
            return accessories;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void logOut(int user, String serviceUrl) {
        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "LogOut";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/LogOut";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("user", user);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getTables(String serviceUrl) {
        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "GetTables";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/GetTables";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            return Integer.parseInt(result.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<Mesa> getTablesArray(String serviceUrl) {
        List<Mesa> mesas = new ArrayList<Mesa>();

        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "GetTablesArray";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/GetTablesArray";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);

            SoapObject resSoap = (SoapObject) envelope.getResponse();

            for (int i = 0; i < resSoap.getPropertyCount(); i++) {
                SoapObject ic = (SoapObject) resSoap.getProperty(i);

                Mesa mesa = new Mesa();
                mesa.setDescripcion(ic.getProperty("Descripcion").toString());
                mesa.setId(Integer.parseInt(ic.getProperty("MesaId").toString()));

                mesas.add(mesa);
            }
            return mesas;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void sendOrder(int waiter, int table, String order, String notes, String serviceUrl) {
        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "SendOrder";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/SendOrder";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("waiter", waiter);
            request.addProperty("table", table);
            request.addProperty("order", order);
            request.addProperty("notes", notes);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean testConnection(String serviceUrl) {
        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "Connected";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/Connected";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            return Boolean.parseBoolean(result.toString());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean changeUserPassword(String user, String oldPassword, String newPassword, String serviceUrl) {
        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "ChangePassword";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/ChangePassword";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("user", user);
            request.addProperty("oldPassword", oldPassword);
            request.addProperty("newPassword", newPassword);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            return Boolean.parseBoolean(result.toString());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ResumenMesa getTableStatus(int waiter, int table, String serviceUrl) {
        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "GetTableStatus";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/GetTableStatus";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("waiter", waiter);
            request.addProperty("table", table);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject soapObject = (SoapObject) envelope.getResponse();
            ResumenMesa resumenMesa = new ResumenMesa();
            resumenMesa.setCantidadPedidos(Integer.parseInt(soapObject.getPrimitivePropertyAsString("CantidadPedidos")));
            resumenMesa.setError(soapObject.getPrimitivePropertyAsString("Error"));
            resumenMesa.setValida(Boolean.parseBoolean(soapObject.getPrimitivePropertyAsString("EsValida")));
            if (resumenMesa.isValida()) {
                SoapObject detalle = (SoapObject) soapObject.getProperty("Detalle");
                for (int i = 0; i < detalle.getPropertyCount(); i++) {
                    SoapObject ic = (SoapObject) detalle.getProperty(i);
                    ResumenMesaDetalle resumenMesaDetalle = new ResumenMesaDetalle();
                    resumenMesaDetalle.setDescripcion(ic.getPrimitivePropertyAsString("Descripcion"));
                    resumenMesaDetalle.setPedidoId(Integer.parseInt(ic.getPrimitivePropertyAsString("PedidoId")));
                    resumenMesaDetalle.setCantidad(ic.getPrimitivePropertyAsString("Cantidad"));
                    resumenMesaDetalle.setFecha(ic.getPrimitivePropertyAsString("Fecha"));
                    resumenMesaDetalle.setAccesorios(ic.getPrimitivePropertyAsString("Accesorios"));
                    resumenMesaDetalle.setAnulado(Boolean.parseBoolean(ic.getPrimitivePropertyAsString("Anulado")));
                    resumenMesaDetalle.setModificado(Boolean.parseBoolean(ic.getPrimitivePropertyAsString("Modificado")));
                    resumenMesaDetalle.setDescargado(Boolean.parseBoolean(ic.getPrimitivePropertyAsString("Descargado")));

                    resumenMesa.getDetalle().add(resumenMesaDetalle);
                }
            }
            return resumenMesa;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Sector> getSectorsForWaiter(int waiter, String serviceUrl) {
        List<Sector> sectors = new ArrayList<>();

        final String NAMESPACE = "http://SGE.Service.Contracts.Service";
        final String URL = serviceUrl;
        final String METHOD_NAME = "GetSectorsForWaiter";
        final String SOAP_ACTION = "http://SGE.Service.Contracts.Service/IOrderService/GetSectorsForWaiter";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("waiterId", waiter);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);

            if (envelope.getResponse() != null) {
                SoapObject sectorsSoap = (SoapObject) envelope.getResponse();

                for (int i = 0; i < sectorsSoap.getPropertyCount(); i++) {
                    SoapObject sectorSoap = (SoapObject) sectorsSoap.getProperty(i);

                    Sector sector = new Sector();
                    sector.setDescripcion(sectorSoap.getProperty("Descripcion").toString());
                    sector.setId(Integer.parseInt(sectorSoap.getProperty("SectorId").toString()));
                    if (sectorSoap.getProperty("Mesas") != null) {
                        SoapObject tablesSoap = (SoapObject) sectorSoap.getProperty("Mesas");
                        for (int j = 0; j < tablesSoap.getPropertyCount(); j++) {
                            SoapObject tableSoap = (SoapObject) tablesSoap.getProperty(j);
                            Mesa mesa = new Mesa();
                            mesa.setDescripcion(tableSoap.getProperty("Descripcion").toString());
                            mesa.setId(Integer.parseInt(tableSoap.getProperty("MesaId").toString()));
                            mesa.setSectorId(sector.getId());
                            mesa.setSectorDescripcion(sector.getDescripcion());
                            sector.getMesas().add(mesa);
                        }
                    }
                    sectors.add(sector);
                }
            }
            return sectors;
        } catch (Exception e) {
            return null;
        }
    }
}
