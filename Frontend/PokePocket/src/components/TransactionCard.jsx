import React from "react";
import { 
  DollarSign, 
  ShoppingCart, 
  Home, 
  FileText, 
  Shirt, 
  PawPrint, 
  Shield, 
  PiggyBank, 
  House, 
  Zap, 
  Utensils, 
  Car, 
  Gamepad, 
  Heart, 
  BookOpen, 
  Scissors, 
  MoreHorizontal, 
  CreditCard, 
  Wallet, 
  Percent, 
  CircleDot 
} from "lucide-react";

const categoryIcons = {
    "Income": DollarSign,
    "Grocery": ShoppingCart,
    "Household supplies": Home,
    "Bills": FileText,
    "Clothing": Shirt,
    "Pets": PawPrint,
    "Insurance": Shield,
    "Savings": PiggyBank,
    "Rent": House,
    "Utilities": Zap,
    "Dining out": Utensils,
    "Transportation": Car,
    "Entertainment": Gamepad,
    "Health care": Heart,
    "Education": BookOpen,
    "Personal care": Scissors,
    "Miscellaneous": MoreHorizontal,
    "Loan": CreditCard,
    "Payments": Wallet,
    "Fees": Percent,
    "Other": CircleDot
  };
  

function TransactionCard({ transaction }) {
    const CategoryIcon = categoryIcons[transaction.category.description];

  return (
    <div
      className="h-15 border-2 rounded-[10px] flex items-center p-2"
      style={{ borderColor: transaction.category.color}}
    >
      <div
        className="h-10 w-10 rounded-[0.5rem] mx-2 flex border-2"
        style={{ backgroundColor: transaction.category.color }}
      >
        <CategoryIcon color="white" className="m-auto" />
      </div>
      <div className="flex flex-col basis-5/12 justify-normal">
        <div className="text-left">{transaction.name}</div>
        <div className="text-stone-500 text-[0.6rem] text-left">
          {transaction.date}
        </div>
      </div>
      <div className="flex justify-end basis-5/12">
        <div className="text-right">
          <div className="text-sm">{transaction.amount} HUF</div>
          <div className="text-stone-500 text-[0.8rem]">{transaction.category.description}</div>
        </div>
      </div>
    </div>
  );
}

export default TransactionCard;
