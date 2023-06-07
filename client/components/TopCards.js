import React from "react";

const TopCards = () => {
    return (
        <div className=" grid lg:grid-cols-5 gap-4 p-4">
            <div className="lg:col-span-2 col-span-1 bg-white justify-between w-full border p-4 rounded-lg">
                <div className="flex flex-col w-full pb-4">
                    <p className="text-gray-600">Total Income</p>
                    <div className="flex items-baseline">
                        <p className="text-2xl font-bold">Rp</p>
                        <p className="text-2xl font-bold">1000.000</p>
                    </div>
                </div>

            </div>
            <div className="lg:col-span-2 col-span-1 bg-white justify-between w-full border p-4 rounded-lg">
                <p className="text-gray-600">Total Expense</p>
                <div className="flex items-baseline">
                        <p className="text-2xl font-bold">Rp</p>
                        <p className="text-2xl font-bold">200.000</p>
                    </div>
            </div>
            <div className=" bg-white justify-between w-full border p-4 rounded-lg">
                <p className="text-gray-600">Balance</p>
                <div className="flex items-baseline">
                        <p className="text-2xl font-bold">Rp</p>
                        <p className="text-2xl font-bold">800.000</p>
                    </div>
            </div>
        </div>

    )
}

export default TopCards;