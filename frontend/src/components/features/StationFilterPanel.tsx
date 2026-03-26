"use client";

import { useState } from "react";
import Button from "@/components/common/Button";
import Select from "@/components/common/Select";
import { StationFilter } from "@/services/station";
import { FILTER_OPTIONS } from "@/data/filterOptions";
import { motion } from "framer-motion";

interface StationFilterPanelProps {
    onDraw: (filter: StationFilter) => void;
    isFetching?: boolean;
}

export default function StationFilterPanel({ onDraw, isFetching = false }: StationFilterPanelProps) {
    const [city, setCity] = useState<string>("");
    const [district, setDistrict] = useState<string>("");

    // Update district list when city changes
    const districtOptions = city && FILTER_OPTIONS.districts[city]
        ? FILTER_OPTIONS.districts[city]
        : [];

    const handleCityChange = (newCity: string) => {
        setCity(newCity);
        setDistrict(""); // Reset district when city changes
    };

    const handleDraw = () => {
        onDraw({
            ...(city ? { city } : {}),
            ...(district ? { district } : {})
        });
    };

    const handleReset = () => {
        setCity("");
        setDistrict("");
    };

    return (
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="w-full max-w-xl mx-auto bg-white dark:bg-slate-800/80 p-6 md:p-8 rounded-3xl shadow-lg border border-slate-100 dark:border-slate-700"
        >
            <div className="text-center mb-8">
                <h2 className="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-pink-500">
                    어떤 역을 찾고 있나요?
                </h2>
                <p className="text-slate-500 mt-2 text-sm">
                    조건을 선택하면 딱 맞는 데이트 장소를 추천해드려요.
                </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-8">
                <Select
                    label="시/도"
                    value={city}
                    onChange={handleCityChange}
                    options={FILTER_OPTIONS.cities.map(c => ({ value: c, label: c }))}
                    placeholder="전체 지역"
                />
                <Select
                    label="구/군"
                    value={district}
                    onChange={setDistrict}
                    options={districtOptions.map(d => ({ value: d, label: d }))}
                    placeholder={city ? "전체 구/군" : "시/도를 먼저 선택해주세요"}
                    disabled={!city || districtOptions.length === 0}
                />
            </div>

            <div className="flex flex-col gap-3">
                <Button
                    onClick={handleDraw}
                    disabled={isFetching}
                    size="lg"
                    icon="casino"
                    className="w-full justify-center"
                >
                    {isFetching ? "찾는 중..." : "랜덤으로 뽑기"}
                </Button>
                {(city || district) && (
                    <button
                        onClick={handleReset}
                        className="text-sm text-slate-500 hover:text-primary transition-colors py-2"
                    >
                        필터 초기화
                    </button>
                )}
            </div>
        </motion.div>
    );
}
