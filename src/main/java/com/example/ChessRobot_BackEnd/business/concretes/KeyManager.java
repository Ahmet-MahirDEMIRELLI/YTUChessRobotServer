package com.example.ChessRobot_BackEnd.business.concretes;

import com.example.ChessRobot_BackEnd.business.abstracts.KeyService;
import com.example.ChessRobot_BackEnd.business.constants.KeyMessages;
import com.example.ChessRobot_BackEnd.core.utilities.results.ErrorResult;
import com.example.ChessRobot_BackEnd.core.utilities.results.Result;
import com.example.ChessRobot_BackEnd.core.utilities.results.SuccessResult;
import org.springframework.beans.factory.annotation.Autowired;

public class KeyManager implements KeyService {

    @Autowired
    public KeyManager() {
        super();
    }

    @Override
    public Result getBackendPublicKey() {
        // read from file or db
        String key = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnlRYnqaHQ8QtnwJsUwCt\n" +
                "BZkNs7dKfoWnp5xNKTAAwNZkdst4VEPQWDX8Z9HzvaGP0Nlcrr/Ms+a/Xa6wNz+v\n" +
                "0STjsfuBKdZ1j9H6ztNKVAlTsJcZAAH6ecZvXkZIK5UjaDPl+8UrNTjFDOWapubE\n" +
                "SFjhYGlLlz4xRXG20zPBmIPO3Sd7f1O6Yfl0jhHsVPO1iYmzc2nu1rSTNFbaU7q2\n" +
                "9TwSRYXrvI+8eFa/UiQNJehbGzk9XS6j/5vtmEcSeFo+g97IfWDOn/zN+VCVCcUc\n" +
                "Xh8c4GVSIixrzi5Ne0omJL9rnKv+lhr/Vy3hOHCDVhJTjH6y642E8L6Z3y9oCRzK\n" +
                "iwIDAQAB\n" +
                "-----END PUBLIC KEY-----";

        if(key == null || key.equals("")){
            return new ErrorResult(KeyMessages.backendPublicKeyNotFound);
        }

        return new SuccessResult(key);
    }
}
