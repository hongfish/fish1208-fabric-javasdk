package main

import (
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
	"fmt"
	"encoding/json"
	"strconv"
)

const (
	codeSuccess   = 200
	codeFailure   = 500
	codeNotExists = 900 //数据不存在
)

type chainCodeResponse struct {
	Code    int
	Message string
}

type Account struct {
	Symbol string
	Balance    int
}

func main()  {
	if err := shim.Start(new(Account)); err != nil {
		fmt.Printf("An error %s occurred during initialization ", err)
	}
}

func (t *Account) Init(stub shim.ChaincodeStubInterface) peer.Response{
	fmt.Println("========== Init Account Chaincode ==========")
	return shim.Success(nil)
}

func (t *Account) Invoke(stub shim.ChaincodeStubInterface) peer.Response{
	fmt.Println("========== Invoke Account Chaincode ==========")
	fn, args := stub.GetFunctionAndParameters()
	switch fn {
	case "saveAccount":
		if len(args) != 2{
			return shim.Error("[AccountChaincode - Invoke]: saveAccount param number is not 2.")
		}
		return t.saveAccount(stub, args)
	case "queryAccount":
		if len(args) != 1{
			return shim.Error("[AccountChaincode - Invoke]: queryAccount param number is not 1.")
		}
		return t.queryAccount(stub, args)
	case "queryBalance":
		if len(args) != 1{
			return shim.Error("[AccountChaincode - Invoke]: queryBalance param number is not 1.")
		}
		return t.queryBalance(stub, args)
	}

	res := stringFormat(codeFailure, "[PersonChaincode - Invoke]: Method not found")
	return shim.Error(res)
}

func (t *Account) saveAccount(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	symbol := args[0]
	t.Symbol = symbol
	account := t.getAccount(stub)
	if account.Symbol != ""{
		res := byteFormat(codeFailure, "[AccountChaincode - saveAccount]: account(symbol=" + symbol + ") already exists.")
		return shim.Success(res)
	}
	balance, err := strconv.Atoi(args[1])
	if err != nil{
		res := stringFormat(codeFailure, "[AccountChaincode - saveAccount]: strconv.Atoi failed." + err.Error())
		return shim.Error(res)
	}
	t.Balance = balance
	ifSuccess := t.setAccount(stub)
	if !ifSuccess{
		res := stringFormat(codeFailure, "[AccountChaincode - saveAccount]: Invoked setAccount failed.")
		return shim.Error(res)
	}
	res := byteFormat(codeSuccess, "[AccountChaincode - saveAccount]: Invoked saveAccount successfully.")
	return shim.Success(res)
}

func (t *Account) queryAccount(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	t.Symbol = args[0]
	account := t.getAccount(stub)
	value, err := json.Marshal(account)
	if err != nil {
		res := stringFormat(codeFailure, "[AccountChaincode - queryAccount]: json.Marshal failed.")
		return shim.Error(res)
	}

	res := byteFormat(codeSuccess, string(value))
	return shim.Success(res)
}

func (t *Account) queryBalance(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	t.Symbol = args[0]
	account := t.getAccount(stub)
	if account.Symbol == ""{
		res := stringFormat(codeFailure, "[AccountChaincode - queryBalance]: account(symbol=" + t.Symbol + ") empty.")
		return shim.Error(res)
	}
	fmt.Println("[AccountChaincode - queryBalance]: Invoked queryBalance successfully.")
	return shim.Success([] byte(strconv.Itoa(t.Balance)))
}

func (t *Account) getAccount(stub shim.ChaincodeStubInterface) Account{
	var account Account
	value, err := stub.GetState(t.Symbol)
	if err != nil{
		fmt.Printf("[AccountChaincode - getAccount]: stub.GetState failed. s%", err)
		return account
	}
	err = json.Unmarshal(value, &account)
	if err != nil{
		return account
	}
	return account
}

func (t *Account) setAccount(stub shim.ChaincodeStubInterface) bool{
	bytes, err := json.Marshal(t)
	if err != nil{
		return false
	}
	err = stub.PutState(t.Symbol, bytes)
	if err != nil{
		return false
	}
	return true
}

func stringFormat(code int, message string) string {
	var response chainCodeResponse
	response.Code = code
	response.Message = message
	byte, err := json.Marshal(response)
	if err != nil {
		fmt.Println("[ReportChaincode - stringFormat]: Invoke json Marshal method failed.")
		return ""
	}

	return string(byte[:])
}

func byteFormat(code int, message string) []byte {
	var response chainCodeResponse
	response.Code = code
	response.Message = message
	byte, err := json.Marshal(response)
	if err != nil {
		fmt.Println("[ReportChaincode - bypeFormat]: Invoke json Marshal method failed.")
		return nil
	}

	return byte
}
