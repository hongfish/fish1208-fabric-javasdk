package main

import (
	"testing"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"fmt"
	"encoding/json"
)

type PayLoad struct {
	Code    int
	Message string
}

func TestReport_func(t *testing.T) {
	scc := new(Report)
	stub := shim.NewMockStub("report",scc)

	reportJSON := "{\"OrderID\":\"1111111111\",\"StudentID\":\"2220131045\",\"SchoolCode\":\"SCH0098\",\"ReportInfo\":{\"ReportID\":\"DLMUANLTHKMCOUODCDAC\",\"PrintDate\":\"2017年06月23日\",\"FileURL\":\"123.jpg\"},\"SchoolInfo\":{\"SchoolName\":\"大连海事大学\",\"SchoolEName\":\"DalianMartimeUniversity\"},\"PersonalInfo\":{\"Name\":\"张茜茹\",\"IDCardNo\":\"441522199501100616\",\"Sex\":\"女\",\"BirthDate\":\"1995年01月10日\"},\"StudentInfo\":{\"StudentID\":\"2220131045\",\"LengthOfSchooling\":\"4年\",\"StartDate\":\"2013年09月\",\"College\":\"交通运输管理学院\",\"Major\":\"国际经济与贸易\",\"Classes\":\"国际经济与贸易2013-1\"}}"
	checkSaveReport(t, stub, reportJSON)

	orderId := "1111111111"
	checkQueryReport(t, stub, orderId)

	checkDeleteReport(t, stub, orderId)

	//checkQueryReport(t, stub, orderId)

}

func checkSaveReport(t *testing.T, stub *shim.MockStub, reportJSON string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("saveReport"), []byte(reportJSON)})
	if res.Status != shim.OK{
		fmt.Println("SaveReport", reportJSON, "failed to save report", string(res.Message))
		t.FailNow()
	}

	if res.Payload == nil {
		fmt.Println("SaveReport", reportJSON, "failed to save report", string(res.Message))
		t.FailNow()
	}
}

func checkQueryReport(t *testing.T, stub *shim.MockStub, orderId string) {
	report := getReport(t, stub, orderId)
	v, err := json.Marshal(report)
	if err != nil {
		fmt.Println("Query Report", orderId, "failed, json Marshal is error.")
		t.FailNow()
	}
	fmt.Println("checkQueryReport", string(v))
}

func checkDeleteReport(t *testing.T, stub *shim.MockStub, orderId string) {
	res := stub.MockInvoke("1", [][]byte{[]byte("deleteReport"), []byte(orderId)})
	if res.Status != shim.OK{
		fmt.Println("deleteReport failed", string(res.Message))
		t.FailNow()
	}

	if res.Payload == nil {
		fmt.Println("deleteReport failed", string(res.Message))
		t.FailNow()
	}
	report := getReport(t, stub, orderId)
	fmt.Println("checkDeleteReport", report.Status)
}

func getReport(t *testing.T, stub *shim.MockStub, orderId string) Report {
	var report  Report
	var payload PayLoad
	res := stub.MockInvoke("1", [][]byte{[]byte("queryReport"), []byte(orderId)})
	if res.Status != shim.OK{
		fmt.Println("Query Report", orderId, "failed", string(res.Message))
		return report
	}

	if res.Payload == nil {
		fmt.Println("Query Report", orderId, "failed", string(res.Message))
		return report
	}

	json.Unmarshal(res.Payload, &payload)
	json.Unmarshal([]byte(payload.Message), &report)
	return report
}
