import { useEffect, useState } from "react";
import { BarChart, CartesianGrid, XAxis, YAxis, Tooltip, Legend, Bar, ResponsiveContainer, LabelList } from "recharts";

function SpendingDiagram({ transactions }) {
    const [diagramData, setDiagramData] = useState([]);

    useEffect(() => {
        const categories = [];

        transactions.forEach(transaction => {
            const category = transaction.category
            categories.map(category => category.description).includes(category.description) || categories.push(category);
        });

        
        const diagramData = [];

        categories.forEach(category => {
            diagramData.push({
                name: category.description,
                spending: transactions
                    .filter(transaction => transaction.category.description === category.description)
                    .reduce((acc, transaction) => acc + transaction.amount, 0),
                fill: category.color
            })
        });

        setDiagramData(diagramData);
    }, [transactions]);

    return (
            <ResponsiveContainer width={`${diagramData.length * 25 <= 100 ? diagramData.length * 25 : 100}%`} height="100%" >
                <BarChart data={diagramData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name"  fontSize={25}/>
                    <Bar dataKey="spending" >
                        <LabelList dataKey="spending" position="insideBottom" fill="#ffffff" fontSize={30}/>
                    </Bar>
                </BarChart>
            </ResponsiveContainer>
    );
}

export default SpendingDiagram;
