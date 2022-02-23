package main

import (
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"fmt"
	"github.com/hyperledger/fabric/protos/peer"
	"encoding/json"
)

const (
	statusValid   = 1
	statusInvalid = 0
)

const (
	codeSuccess   = 200
	codeFailure   = 500
	codeNotExists = 900 //数据不存在
)

const prefix = "Report_"

type Report struct {
	OrderID      string
	StudentID    string
	SchoolCode   string
	ReportInfo   reportInfo
	SchoolInfo   schoolInfo
	PersonalInfo personalInfo
	StudentInfo  studentInfo
	//SubjectInfo  []subjectInfo
	Status       int8  // 是否有效 1:有效 0:无效
}

type reportInfo struct {
	ReportID   string
	PrintDate  string
	//FileURL    string
}

type schoolInfo struct {
	SchoolName  string
	SchoolEName string
}

type personalInfo struct {
	Name      string
	IDCardNo  string
	Sex       string
	BirthDate string
}

type studentInfo struct {
	StudentID         string
	LengthOfSchooling string
	StartDate         string
	College           string
	Major             string
	Classes           string
}

/*
type subjectInfo struct {
	Term    string
	SubjectList []subject
}

type subject struct {
	CourseCode string
	CourseName string
	CourseMode string
	ClassHour  string
	Credit     string
	ScoreType  string
	Score      string
	Point      string
}
*/

type chainCodeResponse struct {
	Code    int
	Message string
}

func main()  {
	if err := shim.Start(new(Report)); err != nil {
		fmt.Printf("An error %s occurred during initialization ", err)
	}
}

func (t *Report) Init(stub shim.ChaincodeStubInterface) peer.Response {
	fmt.Println("========== Init Report Chaincode ==========")
	return shim.Success(nil)
}

func (t *Report) Invoke(stub shim.ChaincodeStubInterface) peer.Response {
	fmt.Println("========== Invoke Report Chaincode ==========")
	fn, args := stub.GetFunctionAndParameters()

	if fn == "saveReport" {
		return t.saveReport(stub, args[0])
	} else if fn == "queryReport" {
		return t.queryReport(stub, args[0])
	} else if fn == "deleteReport" {
		return t.deleteReport(stub, args[0])
	}

	res := stringFormat(codeFailure, "[ReportChaincode - Invoke]: Method not found")
	return shim.Error(res)
}

func (t *Report) saveReport(stub shim.ChaincodeStubInterface, reportJSON string) peer.Response {
	var report Report
	err := json.Unmarshal([]byte(reportJSON), &report)
	if err != nil {
		res := stringFormat(codeFailure, "[ReportChaincode - saveReport]: Converted json to object failed.")
		return shim.Error(res)
	}

	oldReport := t.getReport(stub, report.OrderID)
	if oldReport.OrderID == report.OrderID {
		res := stringFormat(codeFailure, "[ReportChaincode - saveReport]: Report already exists.")
		return shim.Error(res)
	}

	report.Status = statusValid
	ifSuccess := t.setReport(stub, report)
	if !ifSuccess {
		res := stringFormat(codeFailure, "[ReportChaincode - saveReport]: Invoked setReport failed.")
		return shim.Error(res)
	}

	err = stub.SetEvent("saveEvent", byteFormat(codeSuccess,"[ReportChaincode - saveReport]: Save report successfully."))
	if err != nil {
		res := stringFormat(codeFailure, "[ReportChaincode - saveReport]: Save report failed.")
		return shim.Error(res)
	}

	res := byteFormat(codeSuccess, "[ReportChaincode - saveReport]: Invoked saveReport successfully.")
	return shim.Success(res)
}

func (t *Report) queryReport(stub shim.ChaincodeStubInterface, orderId string) peer.Response {
	if orderId == "" {
		res := stringFormat(codeFailure, "[ReportChaincode - queryReport]: orderId cannot be empty.")
		return shim.Error(res)
	}
	report := t.getReport(stub, orderId)
	if report.Status == statusInvalid {
		res := byteFormat(codeNotExists, "[ReportChaincode - queryReport]: data be empty.")
		return shim.Success(res)
	}
	value, err := json.Marshal(report)
	if err != nil {
		res := stringFormat(codeFailure, "[ReportChaincode - queryReport]: Parse json failed when invoked queryReport.")
		return shim.Error(res)
	}

	res := byteFormat(codeSuccess, string(value))
	return shim.Success(res)
}

func (t *Report) deleteReport(stub shim.ChaincodeStubInterface, orderId string) peer.Response {
	report := t.getReport(stub, orderId);
	if report.OrderID != orderId {
		res := stringFormat(codeFailure, "[ReportChaincode - deleteReport]: orderId does not exist.")
		return shim.Error(res)
	}

	report.Status = statusInvalid
	ifSuccess := t.setReport(stub, report)
	if !ifSuccess {
		res := stringFormat(codeFailure, "[ReportChaincode - deleteReport]: Invoked setReport failed.")
		return shim.Error(res)
	}

	res := byteFormat(codeSuccess, "[ReportChaincode - deleteReport]: Invoked deleteReport successfully.")
	return shim.Success(res)
}

func (t *Report) getReport(stub shim.ChaincodeStubInterface, orderId string) Report {
	var report Report
	key := prefix + orderId
	value, err := stub.GetState(key)
	err = json.Unmarshal(value, &report)
	if err != nil {
		return report
	}
	return report
}

func (t *Report) setReport(stub shim.ChaincodeStubInterface, report Report) bool {
	bytes, err := json.Marshal(report)
	if err != nil {
		return false
	}
	key := prefix + report.OrderID
	err = stub.PutState(key, bytes)
	if err != nil {
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

