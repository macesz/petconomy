import React, { useState, useEffect, use } from "react";
import AddExpenseForm from "../../components/AddExpenseForm";
import { fetchData } from "../../utils";
import SpendingDiagram from "../../components/SpendingDiagram";
import SpendingInformation from "../../components/SpendingInformation";
import TransactionCard from "../../components/TransactionCard";

function MyPocket() {
  const [income, setIncome] = useState(500000);
  const [expense, setExpense] = useState(null);
  const [profit, setProfit] = useState(null);
  const [transactions, setTransactions] = useState(null);

  useEffect(() => {
    const jwt = localStorage.getItem("pokePocketJwt");

    fetchData("transactions/all", "GET", null, jwt).then((response) => {
      setTransactions(response);
      const sumWithInitial = response
        .filter((transaction) => transaction.amount > 0)
        .reduce(
          (accumulator, currentValue) => accumulator + currentValue.amount,
          0
        );
      setExpense(sumWithInitial);

      const sumIncome = response
      .filter(transaction => transaction.amount < 0)
      .reduce(
        (accumulator, currentValue) => accumulator + currentValue.amount,
        0
      );
      setIncome(sumIncome);
    });
  }, []);

  useEffect(() => {
    setProfit(income - expense);
  }, [expense, income]);

  return (
    <>
      <div className="bg-white/75 h-[85vh] m-5 text-center p-2 rounded-[15px] mx-auto">
        <div className="grid grid-cols-3 grid-rows-2 h-full gap-2">
          <div className="bg-gray-300 rounded-[15px] items-center flex">
            <button
              className="btn m-auto bg-pokeball p-3 rounded-[15px] text-white"
              onClick={() => document.getElementById("my_modal_4").showModal()}
            >
              Add Expense
            </button>
          </div>
          <SpendingInformation
            profit={profit}
            expense={expense}
            income={income}
          ></SpendingInformation>
          <div className="row-span-2 bg-gray-300 rounded-[15px] p-5">
            <div className="grid grid-cols-1 gap-3">
              <div>Transactions</div>
              {transactions &&
                transactions.map((transaction) => (
                  <TransactionCard
                    key={transaction.id}
                    transaction={transaction}
                  />
                ))}
            </div>
          </div>
          <div className="col-span-2 bg-gray-300 rounded-[15px] ">
            {transactions && <SpendingDiagram transactions={transactions} />}
          </div>
        </div>
      </div>
      <dialog id="my_modal_4" className="modal">
        <div className="modal-box w-11/12  justify-center">
          <AddExpenseForm
            setExpense={setExpense}
            onSetTransactions={setTransactions}
          ></AddExpenseForm>
          <div className="modal-action">
            <form method="dialog">
              {/* if there is a button, it will close the modal */}
              <button className="btn">Close</button>
            </form>
          </div>
        </div>
      </dialog>
    </>
  );
}

export default MyPocket;
