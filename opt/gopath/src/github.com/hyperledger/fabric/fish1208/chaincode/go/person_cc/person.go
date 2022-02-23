package main

import (
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"fmt"
	"github.com/hyperledger/fabric/protos/peer"
	"encoding/json"
	"strconv"
)

const (
	codeSuccess   = 200
	codeFailure   = 500
	codeNotExists = 900 //数据不存在
)

type Person struct {
	Symbol string
	Name   string
	Sex    string
	Age    int
}

type chainCodeResponse struct {
	Code    int
	Message string
}

func main(){
	if err := shim.Start(new(Person)); err != nil{
		fmt.Printf("main() error %s", err)
	}
}

func (t *Person) Init(stub shim.ChaincodeStubInterface) peer.Response {
	fmt.Println("===== Init Chaincode =====")
	return shim.Success(nil)
}

func (t *Person) Invoke(stub shim.ChaincodeStubInterface) peer.Response  {
	fmt.Println("===== Invoke Chaincode =====")
	fn, args := stub.GetFunctionAndParameters()
	switch fn {
	case "savePerson":
		if len(args) != 4{
			return shim.Error("savePerson param number is not 4")
		}
		return t.savePerson(stub, args)
	case "queryPerson":
		if len(args) != 1{
			return shim.Error("queryPerson param number is not 1")
		}
		return t.queryPerson(stub, args)
	case "invokeOtherChaincode":
		if len(args) != 4{
			return shim.Error("invokeOtherChaincode param number is not 1")
		}
		return t.invokeOtherChaincode(stub, args)
	}
	res := stringFormat(codeFailure, "[PersonChaincode - Invoke]: Method not found")
	return shim.Error(res)
}

func (t *Person) savePerson(stub shim.ChaincodeStubInterface, args []string) peer.Response{
	symbol := args[0]
	t.Symbol = symbol
	person := t.getPerson(stub)
	if person.Symbol != ""{
		res := byteFormat(codeFailure, "[PersonChaincode - savePerson]: person(symbol=" + symbol + ") already exists.")
		return shim.Success(res)
	}

	name := args[1]
	sex := args[2]
	age, err := strconv.Atoi(args[3])
	if err != nil{
		res := stringFormat(codeFailure, "[PersonChaincode - savePerson]: strconv.Atoi failed." + err.Error())
		return shim.Error(res)
	}
	t.Name = name
	t.Sex = sex
	t.Age = age
	ifSuccess := t.setPerson(stub)
	if !ifSuccess{
		 res := stringFormat(codeFailure, "[PersonChaincode - savePerson]: setPerson failed.")
		 return shim.Error(res)
	}

	err = stub.SetEvent("saveEvent", byteFormat(codeSuccess,"[PersonChaincode - savePerson]: Save person successfully."))
	if err != nil {
		res := stringFormat(codeFailure, "[PersonChaincode - savePerson]: Save person failed.")
		return shim.Error(res)
	}

	res := byteFormat(codeSuccess, "[PersonChaincode - savePerson]: Invoked successfully.")
	return shim.Success(res)
}

func (t *Person) queryPerson(stub shim.ChaincodeStubInterface, args []string) peer.Response{
	t.Symbol = args[0]
	person := t.getPerson(stub)
	value, err := json.Marshal(person)
	if err != nil{
		res := stringFormat(codeFailure, "[PersonChaincode - queryPerson]: json.Marshal failed." + err.Error())
		return shim.Error(res)
	}
	res := byteFormat(codeSuccess, string(value))
	return shim.Success(res)
}

func (t *Person) invokeOtherChaincode(stub shim.ChaincodeStubInterface, args []string) peer.Response {
	response := stub.InvokeChaincode(args[0], [][]byte{[]byte(args[2]), []byte(args[3])}, args[1])
	if response.Status != shim.OK{
		res := stringFormat(codeFailure, "[PersonChaincode - invokeOtherChaincode]: stub.InvokeChaincode failed." + response.Message)
		return shim.Error(res)
	}
	fmt.Print("[PersonChaincode - invokeOtherChaincode]: Invoked successfully.")
	res := byteFormat(codeSuccess, string(response.Payload))
	return shim.Success(res)
}

func (t *Person) getPerson(stub shim.ChaincodeStubInterface) Person{
	var person Person
	value, err := stub.GetState(t.Symbol)
	if err != nil{
		fmt.Printf("[PersonChaincode - getPerson]: GetState failed. s%", err.Error())
		return person
	}
	err = json.Unmarshal(value, &person)
	if err != nil{
		fmt.Printf("[PersonChaincode - getPerson]: json.Unmarshal failed. s%", err.Error())
		return person
	}
	return person
}

func (t *Person) setPerson(stub shim.ChaincodeStubInterface) bool{
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
