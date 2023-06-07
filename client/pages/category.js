export default function category() {
    return (
      <div>
            <p className="text-gray-700 text-3xl mb-16 font-bold">Category</p>
            <div className=" grid grid-cols-2  gap-4 p-4">
              <div className='bg-white justify-between w-full border p-4 rounded-lg'> Doughnut Chart
                
              </div>
              <div className='bg-white justify-between w-full border p-4 rounded-lg'> Line Chart

              </div>
            </div>

              <div className='bg-white justify-between w-full border p-4 rounded-lg'> Recent Transaction
              </div>

          </div>
    )
  }