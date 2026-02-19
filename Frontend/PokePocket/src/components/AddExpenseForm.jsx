import { useEffect, useState } from "react";
import {fetchData} from "../utils";

function AddExpenseForm({setExpense, onSetTransactions}) {
    const [categories, setCategories] = useState([]);

    const [isFilled, setIsFilled] = useState(false);

    const [category, setCategory] = useState(null);
    const [name, setName] = useState(null);
    const [amount, setAmount] = useState(null);

    const [jwt, setJwt] = useState(localStorage.getItem("pokePocketJwt"));

    useEffect(() => {
        setIsFilled(category && name && amount);
    }, [category, name, amount]);

    useEffect(() => {
        fetchData("transactions/categories/all", "GET", null, jwt)
            .then(response => setCategories(response));
    }, []);

    function handleSubmit(e) {
        e.preventDefault();

        const expenseObject = {
            categoryId: category,
            name: name,
            amount: amount
        };

        const jwt = localStorage.getItem("pokePocketJwt");

        let transactionId;
        fetchData("transactions/add", "POST", expenseObject, jwt)
        .then(response => transactionId = response)
        .then(() => {
            fetchData("transactions/all", "GET", null, jwt).then(response => {
                onSetTransactions(response);
                const sumWithInitial = response.reduce((accumulator, currentValue) => accumulator + currentValue.amount, 0);
                setExpense(sumWithInitial);
            })
        });
      
        document.getElementById("my_modal_4").close();
    }

    return (
        <form onSubmit={handleSubmit}>
            <select defaultValue="Pick a category" className="select" onChange={(event) => setCategory(event.target.value)}>
                <option disabled={true}>Pick a category</option>
                {categories.map(category => (<option key={category.id} value={category.id}>{category.description}</option>))}
            </select>
            <fieldset className="fieldset flex">
                <legend className="fieldset-legend">Name</legend>
                <input type="text" className="input" placeholder="Expense name" onChange={(event) => setName(event.target.value)}/>
            </fieldset>
            <fieldset className="fieldset flex">
                <legend className="fieldset-legend">Amount</legend>
                <input type="number" className="input" placeholder="Expense amount" onChange={(event) => setAmount(event.target.value)}/>
            </fieldset>
            <button type="submit" className="btn btn-primary my-10" disabled={!isFilled}>Add expense</button>
        </form>
    )
}

export default AddExpenseForm;
