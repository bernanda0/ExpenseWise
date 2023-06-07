import { forwardRef } from "react";
import Link from "next/link";
import { HomeIcon,  FolderIcon, ArrowsRightLeftIcon, InformationCircleIcon } from "@heroicons/react/24/solid";
import { useRouter } from "next/router";

const SideBar = forwardRef(({ showNav }, ref) => {
    const router = useRouter();

    return (
        <div ref={ref} className="fixed w-56 h-full bg-white shadow-sm">
            <div className="flex justify-center mt-6 mb-14">
                <picture>
                    <img
                        className="w-32 h-auto"
                        src="/LogoExpenseHitam.png"
                        alt="company logo"
                    />
                </picture>
            </div>

            <div className="flex flex-col">
                <Link href="/">
                    <div
                        className={`pl-6 py-3 mx-5 rounded text-center cursor-pointer mb-3 flex items-center transition-colors ${router.pathname == "/"
                            ? "bg-orange-100 text-orange-500"
                            : "text-gray-400 hover:bg-orange-100 hover:text-orange-500"
                            }`}
                    >
                        <div className="mr-2">
                            <HomeIcon className="h-5 w-5" />
                        </div>
                        <div>
                            <p>Home</p>
                        </div>
                    </div>
                </Link>
                <Link href="/category">
                    <div
                        className={`pl-6 py-3 mx-5 rounded text-center cursor-pointer mb-3 flex items-center transition-colors ${router.pathname == "/category"
                            ? "bg-orange-100 text-orange-500"
                            : "text-gray-400 hover:bg-orange-100 hover:text-orange-500"
                            }`}
                    >
                        <div className="mr-2">
                            <FolderIcon className="h-5 w-5" />
                        </div>
                        <div>
                            <p>Category</p>
                        </div>
                    </div>
                </Link>
                <Link href="/transaction">
                    <div
                        className={`pl-6 py-3 mx-5 rounded text-center cursor-pointer mb-3 flex items-center transition-colors ${router.pathname == "/transaction"
                            ? "bg-orange-100 text-orange-500"
                            : "text-gray-400 hover:bg-orange-100 hover:text-orange-500"
                            }`}
                    >
                        <div className="mr-2">
                            <ArrowsRightLeftIcon className="h-5 w-5" />
                        </div>
                        <div>
                            <p>Transaction</p>
                        </div>
                    </div>
                </Link>

                <div className="mt-40">
                    <Link href="/aboutUs">

                        <div
                            className={`pl-6 py-3 mx-5 rounded text-center cursor-pointer mb-3 flex items-center transition-colors ${router.pathname == "/aboutUs"
                                ? "bg-orange-100 text-orange-500"
                                : "text-gray-400 hover:bg-orange-100 hover:text-orange-500"
                                }`}
                        >
                            <div className="mr-2">
                                <InformationCircleIcon className="h-5 w-5" />
                            </div>
                            <div>
                                <p>About Us</p>
                            </div>
                        </div>
                    </Link>
                </div>
            </div>
        </div>
    );
});

SideBar.displayName = "SideBar";

export default SideBar;