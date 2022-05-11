package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao, JwtService jwtService) {
        this.productDao = productDao;
        this.jwtService = jwtService;
    }

    public List<GetProductList> getProducts() throws BaseException{
        try{
            List<GetProductList> getProductList = productDao.getProducts();
            return getProductList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

  /*  public List<GetProductList> getProductsByProductNo(int productNo) throws BaseException{
        try{
            List<GetProductList> getProductList = productDao.getProductNosByProductNo(productNo);
            return getProductList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }*/


    public GetProductDetail getProductDetail(int productNo) throws BaseException {
        try {
            GetProductDetail getProductDetail = productDao.getProductDetail(productNo);
            return getProductDetail;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkInterestProduct(int userNo, int productNo) throws BaseException{
        try{
            return productDao.checkProductInterest(userNo, productNo);
        } catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetInterestProduct> getInterestProduct(int userNo) throws BaseException {
        try {
            List<GetInterestProduct> getInterestProduct = productDao.getInterestProduct(userNo);
            return getInterestProduct;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSellProduct> getSellProduct(int userNo) throws BaseException {
        try {
            List<GetSellProduct> getSellProduct = productDao.getSellProduct(userNo);
            return getSellProduct;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetSoldOutProduct> getSoldOutProduct(int userNo) throws BaseException {
        try {
            List<GetSoldOutProduct> getSoldOutProduct = productDao.getSoldOutProduct(userNo);
            return getSoldOutProduct;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
