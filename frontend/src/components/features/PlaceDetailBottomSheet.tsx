"use client";

import { AnimatePresence, motion } from "framer-motion";

import Button from "@/components/common/Button";
import { PlaceData } from "@/services/place";
import PlaceDetailPanel from "@/components/features/PlaceDetailPanel";

interface PlaceDetailBottomSheetProps {
  place: PlaceData | null;
  baseLocationName: string;
  isLoading: boolean;
  open: boolean;
  onClose: () => void;
}

export default function PlaceDetailBottomSheet({
  place,
  baseLocationName,
  isLoading,
  open,
  onClose,
}: PlaceDetailBottomSheetProps) {
  return (
    <AnimatePresence>
      {open ? (
        <>
          <motion.button
            type="button"
            aria-label="상세 패널 닫기"
            className="fixed inset-0 z-40 bg-slate-950/50 backdrop-blur-[2px] lg:hidden"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            onClick={onClose}
          />
          <motion.div
            className="fixed inset-x-0 bottom-0 z-50 max-h-[85vh] overflow-hidden lg:hidden"
            initial={{ y: "100%" }}
            animate={{ y: 0 }}
            exit={{ y: "100%" }}
            transition={{ type: "spring", stiffness: 280, damping: 28 }}
          >
            <div className="mx-auto w-full max-w-xl px-3 pb-3">
              <div className="rounded-t-[2rem] bg-transparent">
                <div className="flex justify-center pb-3">
                  <div className="h-1.5 w-12 rounded-full bg-white/70" />
                </div>
                <div className="max-h-[calc(85vh-1.5rem)] overflow-y-auto">
                  <PlaceDetailPanel
                    place={place}
                    baseLocationName={baseLocationName}
                    isLoading={isLoading}
                    layout="sheet"
                  />
                </div>
                <div className="border-x border-b border-slate-200 bg-white px-5 py-4 dark:border-slate-700 dark:bg-slate-950">
                  <Button variant="outline" className="w-full" onClick={onClose}>
                    닫기
                  </Button>
                </div>
              </div>
            </div>
          </motion.div>
        </>
      ) : null}
    </AnimatePresence>
  );
}
