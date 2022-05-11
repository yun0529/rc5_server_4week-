package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexUserId;

@RestController
@RequestMapping("/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    /**
     * 상품 조회 API
     * [GET] /products
     * @return BaseResponse<List<GetProductList>>
     */
   //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/products
    public BaseResponse<List<GetProductList>> getProducts() {
        try{
            //if(productNo == 0){
                List<GetProductList> getProductList = productProvider.getProducts();
                return new BaseResponse<>(getProductList);
            //}
            // Get Users
            //List<GetProductList> getProductList = productProvider.getProductsByProductNo(productNo);
            //return new BaseResponse<>(getProductList);
        } catch(BaseException exception){
            return new BaseResponse<>(((exception.getStatus())));
        }
    }

    /**
     * 상품 세부 조회 API
     * [GET] /products/:productNo
     * @return BaseResponse<GetProductList>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{productNo}") // (GET) 127.0.0.1:9000/products/:productNo
    public BaseResponse<GetProductDetail> getProductDetail(@PathVariable("productNo") int productNo) {
        // Get Users
        try{
            GetProductDetail getProductDetail = productProvider.getProductDetail(productNo);
            return new BaseResponse<>(getProductDetail);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 관심 상품 조회 API
     * [GET] /products/interest/:userNo
     * @return BaseResponse<List<GetInterestProduct>>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/interest/{userNo}") // (GET) 127.0.0.1:9000/product/interest/:userNo
    public BaseResponse<List<GetInterestProduct>> getInterestProduct(@PathVariable("userNo") int userNo) {
        // Get Users
        try{
            List<GetInterestProduct> getInterestProduct = productProvider.getInterestProduct(userNo);
            return new BaseResponse<>(getInterestProduct);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    /**
     * 관심 상품 등록 API
     * [POST] /products/interest
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/registInterest")
    public BaseResponse<String> registInterest(@RequestBody PostInterestReq postInterestReq) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postInterestReq.getUserNo() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //관심 정보가 존재하면 patch하고 아니면 post하게 설정해야될듯
            productService.registInterest(postInterestReq);
            //PostUserRes postUserRes = userService.createUser(postUserReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 등록 API
     * [POST] /products
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("{userNo}") // (POST) 127.0.0.1:9000/products
    public BaseResponse<String> postProduct(@PathVariable("userNo") int userNo, @RequestBody PostProduct postProduct) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userNo != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(postProduct.getProductTitle() == null){
                //타이틀 입력하라는 에러 메시지
                return new BaseResponse<>(POST_PRODUCTS_EMPTY_TITLE);
            }
            if(postProduct.getProductContent() == null){
                // 내용을 입력하라는 에러 메시지
                return new BaseResponse<>(POST_PRODUCTS_EMPTY_CONTENT);
            }
            productService.postProduct(userNo, postProduct);
            //PostUserRes postUserRes = userService.createUser(postUserReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 판매 내역 조회 API
     * [GET] /products/sell/:productNo
     * @return BaseResponse<List<GetSellProduct>>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/sell/{userNo}") // (GET) 127.0.0.1:9000/products/sell/:userNo
    public BaseResponse<List<GetSellProduct>> getSellProduct(@PathVariable("userNo") int userNo) {
        try{
            List<GetSellProduct> getSellProduct = productProvider.getSellProduct(userNo);
            return new BaseResponse<>(getSellProduct);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    /**
     * 거래완료 내역 조회 API
     * [GET] /products/soldOut/:productNo
     * @return BaseResponse<List<GetSoldOutProduct>>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/soldOut/{userNo}") // (GET) 127.0.0.1:9000/products/soldOut/:userNo
    public BaseResponse<List<GetSoldOutProduct>> getSoldOutProduct(@PathVariable("userNo") int userNo) {
        try{
            List<GetSoldOutProduct> getSoldOutProduct = productProvider.getSoldOutProduct(userNo);
            return new BaseResponse<>(getSoldOutProduct);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 판매 상태 변경 API
     * [PATCH] /products/productStatus/:userNo
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/productStatus/{userNo}") // 127.0.0.1:9000/products/productStatus/:userNo
    public BaseResponse<String> patchProductStatus(@PathVariable int userNo, @RequestBody PatchProductStatus patchProductStatus) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userNo != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            productService.patchProductStatus(userNo,patchProductStatus);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
