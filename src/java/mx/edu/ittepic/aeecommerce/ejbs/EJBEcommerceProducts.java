/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.ejbs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import mx.edu.ittepic.aeecommerce.entities.Category;
import mx.edu.ittepic.aeecommerce.entities.Product;
import mx.edu.ittepic.aeecommerce.util.Image;
import mx.edu.ittepic.aeecommerce.util.Message;

/**
 *
 * @author gustavo
 */
@Stateless
public class EJBEcommerceProducts {

    @PersistenceContext
    private EntityManager entity;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public String newProduct(String code, String brand, String purchprice, String productname, String stock,
            String salepricemin, String salepricemay, String reorderpoint, String categoryid, String curency, Image image) {

        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            Product p = new Product();
            Category c = entity.find(Category.class, Integer.parseInt(categoryid));
            p.setCode(code);
            p.setBrand(brand);
            p.setPurchprice(Double.parseDouble(purchprice));
            p.setProductname(productname);
            p.setStock(Integer.parseInt(stock));
            p.setSalepricemin(Double.parseDouble(salepricemin));
            p.setSalepricemay(Double.parseDouble(salepricemay));
            p.setReorderpoint(Integer.parseInt(reorderpoint));
            p.setCurrency(curency);

            p.setCategoryid(c);

            Path file;
            try {

                try (InputStream input = image.getContent()) {
                    if (input.available() > 0) {
                        file = Files.createTempFile(Paths.get("/var/www/images/products/"), "product-", ".png");
                        Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                        p.setImage(file.getFileName().toString());
                    } else {
                        p.setImage("product.png");
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(EJBEcommerceUsers.class.getName()).log(Level.SEVERE, null, ex);
            }

            entity.persist(p);
            entity.flush();

            m.setCode(200);
            m.setMsg("todo hermoso");
            m.setDetail(p.getProductid().toString());

        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        } catch (PersistenceException e) {
            m.setCode(500);
            m.setMsg("Error, Algo salió mal :(, vuelve a intentarlo");
            m.setDetail(e.getMessage());
        }

        return gson.toJson(m);

    }

    public String updateProduct(String id, String code, String brand, String purchprice, String productname, String stock,
            String salepricemin, String salepricemay, String reorderpoint, String categoryid, String curency, Image image) {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Product p = entity.find(Product.class, Integer.parseInt(id));
        try {
            if (p != null) {
                Category c = entity.find(Category.class, Integer.parseInt(categoryid));
                p.setCode(code);
                p.setBrand(brand);
                p.setPurchprice(Double.parseDouble(purchprice));
                p.setProductname(productname);
                p.setStock(Integer.parseInt(stock));
                p.setSalepricemin(Double.parseDouble(salepricemin));
                p.setSalepricemay(Double.parseDouble(salepricemay));
                p.setReorderpoint(Integer.parseInt(reorderpoint));
                p.setCurrency(curency);
                p.setCategoryid(c);

                Path file;
                try {

                    try (InputStream input = image.getContent()) {
                        if (input.available() > 0) {
                            file = Files.createTempFile(Paths.get("/var/www/images/products/"), "product-", ".png");
                            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                            p.setImage(file.getFileName().toString());
                        } else {
                            p.setImage("product.png");
                        }

                    }
                } catch (IOException ex) {
                    Logger.getLogger(EJBEcommerceUsers.class.getName()).log(Level.SEVERE, null, ex);
                }

                entity.merge(p);
                //entity.flush();
                m.setCode(200);
                m.setMsg("ok");
                m.setDetail("todo hermoso tmb");
            } else {
                m.setCode(404);
                m.setMsg("Error");
                m.setDetail("No encontrado");
            }
        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        }

        return gson.toJson(m);
    }

    public String findProduct(String id) {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Query q;
        Product p;
        try {
            p = entity.find(Product.class, Integer.parseInt(id));
            if (p != null) {
                m.setCode(200);
                m.setMsg(gson.toJson(p));
                m.setDetail("ok");
            } else {
                m.setCode(404);
                m.setMsg("No se encontró");
                m.setDetail("Error");
            }

        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        } catch (QueryTimeoutException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (PessimisticLockException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (LockTimeoutException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (PersistenceException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        }

        return gson.toJson(m);

    }

    public String findProductsByReorder() {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Query q;
        List<Product> productList;
        try {
            q = entity.createNamedQuery("Product.findAllByReorder");
            productList = q.getResultList();
            m.setCode(200);
            m.setMsg(gson.toJson(productList));
            m.setDetail("ok");
        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        } catch (QueryTimeoutException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (PessimisticLockException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (LockTimeoutException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (PersistenceException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        }
        return gson.toJson(m);
    }

    public String findProductByName(String name) {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // Query q;
        Product p;
        try {
            p = (Product) entity.createNamedQuery("Product.findByProductname").setParameter("productname", name).getSingleResult();
            if (p != null) {
                m.setCode(200);
                m.setMsg(gson.toJson(p));
                m.setDetail("ok");
            } else {
                m.setCode(404);
                m.setMsg("No se encontró");
                m.setDetail("Error");
            }

        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        } catch (QueryTimeoutException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (PessimisticLockException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (LockTimeoutException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (PersistenceException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        }

        return gson.toJson(m);

    }

    public String findProducts() {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Query q;
        List<Product> listProduct;

        try {
            q = entity.createNamedQuery("Product.findAll");
            listProduct = q.getResultList();
            m.setCode(200);
            m.setMsg(gson.toJson(listProduct));
            m.setDetail("todo hermoso tmb");

        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        } catch (QueryTimeoutException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (PessimisticLockException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (LockTimeoutException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        } catch (PersistenceException e) {
            m.setCode(500);
            m.setMsg("Error, algo paso en el server");
            m.setDetail(e.getMessage());
        }

        return gson.toJson(m);

    }

    public String deleteProduct(String id) {
        Message m = new Message();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            Product p = entity.find(Product.class, Integer.parseInt(id));
            if (p != null) {
                if (!p.getImage().equals("product.png")) {
                    Files.delete(Paths.get("/var/www/images/products/" + p.getImage()));
                }
                entity.remove(p);
                m.setCode(200);
                m.setMsg("ok");
                m.setDetail("todo hermoso tmb");

            } else {
                m.setCode(404);
                m.setMsg("Error");
                m.setDetail("No encontrado");
            }
        } catch (IllegalArgumentException e) {
            m.setCode(406);
            m.setMsg("Error, tipo de dato invalido");
            m.setDetail(e.getMessage());
        } catch (TransactionRequiredException e) {
            m.setCode(403);
            m.setMsg("Error, prohibido");
            m.setDetail(e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(EJBEcommerceProducts.class.getName()).log(Level.SEVERE, null, ex);
            m.setCode(403);
            m.setMsg("Error, un problema pasó con la imagen");
            m.setDetail(ex.getMessage());
        }
        return gson.toJson(m);
    }
}
