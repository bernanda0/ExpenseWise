'use client'
import TopCards from '@/components/TopCards.js';
import Login from './login.js';
import { useState } from 'react';
import DoughnutChart from '@/components/DoughnutChart.js';
import { Line } from 'react-chartjs-2';
import TableTransaction from '@/components/TableTransaction.js';


export default function Home() {
  const [isLoggedIn, setIsLoggedIn] = useState(true);
  return (
    <>

      <title>Expense Wise</title>
      <div>
        {isLoggedIn ? (
          // Tampilkan halaman utama jika pengguna sudah login
          <div>
            <p className="text-gray-700 text-3xl mb-16 font-bold">Dashboard</p>
            <TopCards />
            <div className=" grid grid-cols-2  gap-4 p-4">
              <div className='bg-white justify-between w-full border p-4 rounded-lg'> Doughnut Chart
                <DoughnutChart />
              </div>
              <div className='bg-white justify-between w-full border p-4 rounded-lg'> Line Chart
              </div>
            </div>

              <div className='bg-white justify-between w-full border p-4 rounded-lg mt-8'> Recent Transaction
                <TableTransaction/>
              </div>

          </div>
        ) : (
          // Tampilkan halaman login jika pengguna belum login
          <Login setIsLoggedIn={setIsLoggedIn} />
        )}
      </div>

    </>
  );
}